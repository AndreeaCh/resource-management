# Getting the log of a resource

A client can query the log of a resource by sending the following message:

``` javascript
MESSAGE
id:lock-req-1
destination:/topic/getResourceLog
[
  {
    "plateNumber": "CJ05ACH",
    "resourceStatus": "AVAILABLE_IN_GARAGE"
  }
]
```

The response contains the log for the resource:

``` javascript
MESSAGE
subscription:sub-1
message-id:sub-notification-123
destination:/topic/resourceLogs
content-type:text/json

[
 "resourceLogs": [
        {
          "changedAt": "2018-11-20T10:49:48Z",
          "changedBy": "10.17.23.32",
          "status": "IN_INTERVENTION"
        },
        {
          "changedAt": "2018-11-20T10:49:33Z",
          "changedBy": "10.17.23.32",
          "status": "AVAILABLE_ON_ROUTE"
        },
        {
          "changedAt": "2018-11-20T10:49:39Z",
          "changedBy": "10.17.23.32",
          "status": "AVAILABLE_IN_GARAGE"
        }
      ]
]
```
