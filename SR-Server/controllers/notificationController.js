
var FCM = require('fcm-node');
var serverKey = 'AAAArqKo9Tw:APA91bEMMcM7pv-9QVhPyT5vpuMCgWftkmEvADhuaCF1aSBs-JA4e2JKISJr4_8v0fiUmQj5PW7vNDsKFT9COP780gq7NQgwOyBJcaarmFJ69FYLd7EGG-Ty9qp37NmEmJ9XWXRPha2A'; //put your server key here
var fcm = new FCM(serverKey);

module.exports={

    test(req, res) {

        var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
            to: 'eXtgkfmV7pM:APA91bHplnZg5ApvPv4O3didrzL8wmnqwOp-nv0Zhu8LZOPa6PIHzThwqYld4iqtqEUNSJh19ANTw33HUshepGFRfMHppBmjq_v31KxspOfpbol8gQHO5-do00jb7CU4i49P3I5k1CVh', 
            notification: {
                title: 'Weleeeeeeee', 
                body: 'I love uuuuuu' 
            },
            
            data: {  //you can send only notification or only data(or include both)
                my_key: 'my value',
                my_another_key: 'my another value'
            }
        };

        return fcm.send(message, function(err, response){
            if (err) {
                console.log("Something has gone wrong!");
                   res.json({result:"failes"});
    
            } else {
                console.log("Successfully sent with response: ", response);
                res.json({result:"success"});
    
            }
        });


    
    },
  


    

};
