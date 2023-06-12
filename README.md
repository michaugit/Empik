# Empik reqruitment assessment
## The assessment
Create a simple REST service that will return information
- ID
- login
- Name
- Type
- Url to avatar
- Creation date
- Calculations

The service API should look like this:
```
GET /users/{login}
{
"id": "...",
"login": "...",
"name": "...",
"type": "...",
"avatarUrl": "",
"createdAt": "..."
"calculations": "..."
}
```
The service should download data from https://api.github.com/users/{login} (e.g. https://api.github.com/users/octocat) and pass it on to the API. The calculations field should return 6 / number_followers * (2 + number_public_repos).

The website should record the number of API calls for each login in the database.

The database should contain two columns: LOGIN and REQUEST_COUNT. REQUEST_COUNT should be incremented by one for each service call.

## Technological stack of implementation
- Java 17
- SpringBoot 3.1.0
- Database: PostgreSQL