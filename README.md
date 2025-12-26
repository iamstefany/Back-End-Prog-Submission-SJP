# ERD
![alt text](docs/erd.png)


# How to generate JWT Secret
- On Linux / macOS: `openssl rand -base64 64`
- On Windows (PowerShell): `[Convert]::ToBase64String((1..64 | ForEach-Object {Get-Random -Maximum 256}))`


# API Endpoints
TODO