import boto3
from botocore.exceptions import ClientError

dynamodb = boto3.resource('dynamodb')
table_name = 'YourTransactionHistoryTableName'
table = dynamodb.Table(table_name)

def get_transaction_history(event, context):
    try:
        account_id = event['account_id']

        response = table.query(
            KeyConditionExpression='account_id = :id',
            ExpressionAttributeValues={':id': account_id}
        )

        transactions = response.get('Items', [])

        return {
            'statusCode': 200,
            'body': transactions
        }

    except ClientError as e:
        return {
            'statusCode': 500,
            'body': str(e)
        }