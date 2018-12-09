# Editing a Sub-Unit

A client can start editing a sub-unit by sending the following message:

``` javascript
MESSAGE
id:lock-req-1
destination:/topic/addSubUnit
[
  {
    "name": "CJ"
  }
]
```

The response can be a success or a failure :

``` javascript
MESSAGE
id:lock-req-1
destination:/topic/subunits
[
  {
    "statusCode": "OK"
  }
]
```

``` javascript
MESSAGE
id:lock-req-1
destination:/topic/subunits
[
  {
    "statusCode": "ERROR"
  }
]
```

When a sub-unit is added all the clients will receive the following message:

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
        "status": "AVAILABLE_IN_GARAGE",
        "type": "FIRST_INTERVENTION"
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
        "status": "IN_INTERVENTION",
        "type": "OTHER"
      }
    ]
  }
]
```
