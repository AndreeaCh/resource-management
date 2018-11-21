# Changing the status for a resource

A client can change the status of a resource by sending the following message:

``` javascript
MESSAGE
id:lock-req-1
destination:/topic/updateStatus
[
  {
    "plateNumber": "CJ05ACH",
    "resourceStatus": "AVAILABLE_IN_GARAGE"
  }
]
```

When the status of a resource changes all the clients will receive the following message:

``` javascript
MESSAGE
subscription:sub-1
message-id:sub-notification-123
destination:/topic/unitUpdatedNotification
content-type:text/json

[
  {
    "name": "CJ",
    "resources": [
      {
        "identificationNumber": "1234",
        "plateNumber": "CJ04ASD",
        "vehicleType": "APCA",
        "captain": "Pt. Major John Doe",
        "crew": [
          "Lt. Anne Doe",
          "Ben Doe"
        ],
        "status": "AVAILABLE_IN_GARAGE"
      },
      {
        "identificationNumber": "1235",
        "plateNumber": "CJ05ASD",
        "vehicleType": "EPA",
        "captain": "Pt. Major Jack Moe",
        "crew": [
          "Lt. Anne Moe",
          "Ben Moe"
        ],
        "status": "IN_INTERVENTION"
      }
    ]
  }
]
```
