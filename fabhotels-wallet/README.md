## About

This project is about  *Building a Java library to build the wallet application to track users transaction account.*

This is a spring boot application with in memory database *H2*. I am using JPA(Java Persistance API) to interact with the in memory database.

## How to run the project

Following steps illustrate procedures you need to follow to run the code :

`Step 1` : Clone the project in STS from below path

 Path : `https://github.com/yadavs2511/wallet-application`

`Step 2` : Import maven project

`Step 3` : Run as spring boot project

## Functionality

Since project uses *H2* in-memory database, some sample data has already been provided to get started with.

* You can see the entries in the table . Navigate to `http://localhost:8086/h2-console` .
* 
**Make sure**  that you use `jdbc:h2:mem:testdb` as JDBC URL. Click connect.

Enter below select queries to see the output :

```{sql}
select * from customer;
select * from wallet;
select * from account;
select * from bank_transaction;
```


Now, I think you are all set up. Lets see what this application can do :

* ### 1) Create an new user :

Provided endpoint : `http://localhost:8086/api/createUser`

Method: POST

use request as :
```{JSON}
{
    "userId": 5,
    "fname": "fab",
    "lname": "hotel",
    "email": "hotel@gmail.com",
    "password": "gurgaon123"
}
```
* ### 2) Login for existing user :

Method: GET

Provided endpoint : `http://localhost:8086/api/userLogin?userId=3&password=abcd`

```
Pass UserId and Password as request Parameters.
```
This will return user details if userid and password are correct else it will throw exception.

* ### 3) Add an account to user 4 (Dan in this case) :

Provided endpoint : `http://localhost:8086/api/createAccount`

Method: POST

use request as :
```{JSON}
{
	"balance":4000,
	"accountHolder" : {
		"userId": 4,
		"fname" : "Dan",
		"lname":"Brown",
		"email" :"dan@brown"
	}
}
```

* ### 4) Create a new wallet for a user :

I am assuming a user can have multiple accounts.

Provided endpoint for creating new wallet :

Method: POST

```
http://localhost:8086/api/createWallet/{userId}
```

* ### 5) Return current account balance :

Method: GET

Provided endpoint : `http://localhost:8086/api/wallet/{walletId}/account/{accountId}/getAccountBalance`.

 * pass account number as account id and wallet holder id as wallet id.

The backend JAVA code checks for all validation. If the accountId is not associated with provided walledId, an exception is thrown.

* ### 6) Perform a addAmount transaction on an account :
* 
Method: POST

Provided endpoint : `http://localhost:8086/api/wallet/{walletId}/account/{accountId}/addAmount/{amount}`

Allows one to deposit amount into an account associated with a wallet.


* ### 7) Perform a transfer from one account to another account :
* 
Method: POST

Provided endpoint :
`http://localhost:8086/api/wallet/{walletId}/account/{trasferFromAccountId}/transfer/wallet/{toWalletId}/account/{transferToAccountId}/amount/{amount}`.


Allows one to transfer money from one account in one wallet to another account in another wallet.

* ### 8) Return last N transactions for an account :
* 
Method: GET

Provided endpoint :  `http://localhost:8086/api/wallet/{walletId}/account/{accountId}/getLastNTransactions/{n}`.

Allows one to check their respect transaction statement.

