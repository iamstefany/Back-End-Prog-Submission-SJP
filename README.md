# ERD
![alt text](docs/erd.png)


# How to generate JWT Secret
- On Linux / macOS: `openssl rand -base64 64`
- On Windows (PowerShell): `[Convert]::ToBase64String((1..64 | ForEach-Object {Get-Random -Maximum 256}))`


# API Endpoints

## <code style="color:#008000;">[GET]</code> auth/info

Get info about the currently logged-in user. Needs valid Bearer Token.

#### Headers:
```
Authorizarion: Bearer <BearerToken>
```

#### Response:
```json
{
  "userId": "<UUID>",
  "firstName": "<String>",
  "lastName": "<String>",
  "email": "<String>",
  "isBlocked": "<Boolean>",
  "roleNames": [
    "GUEST",
    "HOST",
    "ADMIN"
  ],
  "accessToken": "<String>"
}
```

## <code style="color:#FFAE00;">[POST]</code> auth/register/:role

Register a new user. Role can be `guest` or `host`.

#### Body (JSON)
```json
{
  "email": "<String>",
  "password": "<String>",
  "firstName": "<String>",
  "lastName": "<String>",
  "phoneNumber": "<String - Required if role is 'guest'>",
  "dateOfBirth": "<YYYY-MM-DD - Required if role is 'guest'>"
}
```

#### Response:
```json
{
    "userId": "<UUID>"
}
```

## <code style="color:#FFAE00;">[POST]</code> auth/login 

Login a user. Please note:
- The Postman collection has ready-to-use endpoints to log in with each role for easier testing:
  - Login as Guest
  - Login as Host
  - Login as Guest & Host User (both roles)
  - Login as Basic Admin
  - Login as Super Admin
  - Login as Blocked User
- Blocked users can still log in by design:
  - Login for a blocked user is allowed
  - However, blocked users cannot perform any actions other than 
  viewing their own information via `/auth/info`.
  - This is an intentional design choice to allow blocked users to:
    - View their account details 
    - Access contact information for support 
    - (Hypothetically) contact an administrator to request unblocking

#### Body (JSON)
```json
{
  "email": "<String>",
  "password": "<String>",
  "firstName": "<String>",
  "lastName": "<String>",
  "phoneNumber": "<String - Required if role is 'guest'>",
  "dateOfBirth": "<YYYY-MM-DD - Required if role is 'guest'>"
}
```

#### Response:
```json
{
    "accessToken": "<String>"
}
```


## <code style="color:#008000;">[GET]</code> user/list/

Get all users. Please note: Only admins are allowed to access this endpoint.

#### Headers:
```
Authorizarion: Bearer <BearerToken>
```

#### Response:
```json lines
[
  {
    "userId": "<UUID>",
    "email": "<String>",
    "firstName": "<String>",
    "lastName": "<String>",
    "profileImageUrl": "<String>",
    "registrationDate": "<YYYY-MM-DD>",
    "isBlocked": "<Boolean>",
    "roles": "<Integer>",
    "roleNames": [ "GUEST", "HOST", "ADMIN" ],
    "adminProfile": {
      "userId": "<UUID>",
      "isSuperAdmin": "<Boolean>"
    },
    "guestProfile": {
      "userId": "<UUID>",
      "dateOfBirth": "<YYYY-MM-DD>",
      "phoneNumber": "<String>"
    },
    "hostProfile": {
      "userId": "<UUID>",
      "hostSince": "<YYYY-MM-DD>",
      "hostVerified": "<Boolean>"
    }
  }
  // , ... more users
]
```


## <code style="color:#008000;">[GET]</code> user/:user_id/

Get user details by id. Please note:
- Only admins can get details of other users.
- Regular users can only get their own details.

#### Headers:
```
Authorizarion: Bearer <BearerToken>
```

#### Response:
```json
{
  "userId": "<UUID>",
  "email": "<String>",
  "firstName": "<String>",
  "lastName": "<String>",
  "profileImageUrl": "<String>",
  "registrationDate": "<YYYY-MM-DD>",
  "isBlocked": "<Boolean>",
  "roles": "<Integer>",
  "roleNames": [ "GUEST", "HOST", "ADMIN" ],
  "adminProfile": {
    "userId": "<UUID>",
    "isSuperAdmin": "<Boolean>"
  },
  "guestProfile": {
    "userId": "<UUID>",
    "dateOfBirth": "<YYYY-MM-DD>",
    "phoneNumber": "<String>"
  },
  "hostProfile": {
    "userId": "<UUID>",
    "hostSince": "<YYYY-MM-DD>",
    "hostVerified": "<Boolean>"
  }
}
```

## <code style="color:#FFAE00;">[POST]</code> user/:user_id/upload-profile-picture/

Upload profile picture for given user id. Please note:
- Only admins can upload profile pictures for other users.
- Regular users can only upload their own profile picture.

#### Headers:
```
Authorizarion: Bearer <BearerToken>,
Content-Type: multipart/form-data
```

#### Body (form-data):
```json
{
  "file": "<File>"
}
```

#### Response:
```json
{
  "profile-pic": "<String>"
}
```


## <code style="color:#9370DBFF;">[PATCH]</code> user/:user_id/

Update user details by id. Please note:
- Only admins can update details of other users.
- Regular users can only update their own details.
- Passwords cannot be updated via this endpoint.
  - To change password, use dedicated password change endpoint.
- Only guests are allowed to update their phone number and date of birth.

#### Headers:
```
Authorizarion: Bearer <BearerToken>
```

#### Body:
```json
{
  "firstName": "<String>", 
  "lastName": "<String>",
  "dateOfBirth": "<YYYY-MM-DD>",
  "phoneNumber": "<String>"
}
```

#### Response:
```json
{
  "userId": "<UUID>",
  "email": "<String>",
  "firstName": "<String>",
  "lastName": "<String>",
  "profileImageUrl": "<String>",
  "registrationDate": "<YYYY-MM-DD>",
  "isBlocked": "<Boolean>",
  "roles": "<Integer>",
  "roleNames": [ "GUEST", "HOST", "ADMIN" ],
  "adminProfile": {
    "userId": "<UUID>",
    "isSuperAdmin": "<Boolean>"
  },
  "guestProfile": {
    "userId": "<UUID>",
    "dateOfBirth": "<YYYY-MM-DD>",
    "phoneNumber": "<String>"
  },
  "hostProfile": {
    "userId": "<UUID>",
    "hostSince": "<YYYY-MM-DD>",
    "hostVerified": "<Boolean>"
  }
}
```

