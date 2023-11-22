import boto3
from botocore.exceptions import ClientError
import decimal

dynamodb = boto3.resource('dynamodb')
table_name = 'transactions'
table = dynamodb.Table(table_name)

def transfer_funds(event, context):
    try:
        sender_account_id = event['sender_account_id']
        receiver_account_id = event['receiver_account_id']
        amount = decimal.Decimal(event['amount'])

        sender_account = table.get_item(Key={'account_id': sender_account_id})
        receiver_account = table.get_item(Key={'account_id': receiver_account_id})

        if 'Item' not in sender_account or 'Item' not in receiver_account:
            return {
                'statusCode': 404,
                'body': 'Sender or receiver account not found'
            }

        sender_balance = decimal.Decimal(sender_account['Item']['balance'])
        receiver_balance = decimal.Decimal(receiver_account['Item']['balance'])

        if sender_balance < amount:
            return {
                'statusCode': 400,
                'body': 'Insufficient funds'
            }

        sender_balance -= amount
        receiver_balance += amount

        table.update_item(
            Key={'account_id': sender_account_id},
            UpdateExpression='SET balance = :val',
            ExpressionAttributeValues={':val': sender_balance}
        )

        table.update_item(
            Key={'account_id': receiver_account_id},
            UpdateExpression='SET balance = :val',
            ExpressionAttributeValues={':val': receiver_balance}
        )

        return {
            'statusCode': 200,
            'body': 'Transfer successful'
        }

    except ClientError as e:
        return {
            'statusCode': 500,
            'body': str(e)
        }