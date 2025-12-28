# ERD
![alt text](docs/erd.png)


# How to generate JWT Secret
- On Linux / macOS: `openssl rand -base64 64`
- On Windows (PowerShell): `[Convert]::ToBase64String((1..64 | ForEach-Object {Get-Random -Maximum 256}))`


# API Endpoints

## [GET] /auth/info

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

## [POST] /auth/register/:role

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

## [POST] /auth/login 

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


## [GET] /user/list

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


## [GET] /user/:user_id

Get user details by id. Please note:
- Only admins can get details of other admins.
- Regular users can only get details of Host or Guest users.

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

## [POST] /user/:user_id/upload-profile-picture

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


## [PATCH] /user/:user_id

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


## [DELETE] /user/:user_id

Delete user by id. Please note:
- Only admins can delete other users.
- Regular users can only delete their own account.

#### Headers:
```
Authorizarion: Bearer <BearerToken>
```

#### Response:
```
204 No Content
```


## [GET] /property/:property_id

Get property details by id.

#### Headers:
```
Authorizarion: Bearer <BearerToken>
```

#### Response:
```json
{
  "title": "<String>",
  "description": "<String>",
  "address": "<String>",
  "city": "<String>",
  "country": "<String>",
  "pricePerNight": "<Decimal>",
  "maxGuests": "<Integer>",
  "automaticConfirmation": "<Boolean>",
  "checkInTime": "<HH:mm:ss>",
  "checkOutTime": "<HH:mm:ss>",
  "user": { },
  "amenities": [ ],
  "images": [ ],
  "propertyId": "7cf3291b-4489-40eb-ab7b-18ae370cc3d8"
}
```


## [POST] /property

Creates a property. Please note:
- Only hosts can access this endpoint.
- To make the endpoint easier to use, whenever a property is created, it is assigned:
  - A set of random amenities
  - A set of random property images (from pexels.com API)
- By design, properties are only allowed for:
  - Country: IT
  - City: 'Torino', 'Milano', 'Roma', or 'Napoli'
  - This choice was made to ensure easier testing on properties search

#### Headers:
```
Authorizarion: Bearer <BearerToken>
```

#### Body:
```json
{
  "title": "<String>",
  "description": "<String>",
  "address": "<String>",
  "city": "<String>",
  "country": "<String>",
  "pricePerNight": "<Decimal>",
  "maxGuests": "<Integer>",
  "automaticConfirmation": "<Boolean - optional>",
  "checkInTime": "<HH:mm:ss - optional>",
  "checkOutTime": "<HH:mm:ss - optional>"
}
```

#### Response:
```json
{
  "title": "<String>",
  "description": "<String>",
  "address": "<String>",
  "city": "<String>",
  "country": "<String>",
  "pricePerNight": "<Decimal>",
  "maxGuests": "<Integer>",
  "automaticConfirmation": "<Boolean>",
  "checkInTime": "<HH:mm:ss>",
  "checkOutTime": "<HH:mm:ss>",
  "user": { },
  "amenities": [ ],
  "images": [ ],
  "propertyId": "7cf3291b-4489-40eb-ab7b-18ae370cc3d8"
}
```