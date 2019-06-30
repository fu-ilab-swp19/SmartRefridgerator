const User =require('../models').User;
var FCM = require('fcm-node');
var serverKey = 'AAAArqKo9Tw:APA91bEMMcM7pv-9QVhPyT5vpuMCgWftkmEvADhuaCF1aSBs-JA4e2JKISJr4_8v0fiUmQj5PW7vNDsKFT9COP780gq7NQgwOyBJcaarmFJ69FYLd7EGG-Ty9qp37NmEmJ9XWXRPha2A'; //put your server key here
var fcm = new FCM(serverKey);

module.exports={

    test(req, res) {

        var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
            to: 'dwJz1Tdbd68:APA91bGnt5NhOpXMrnwdAQ-RLsY1MXijcm6D0eIOa-Zk538SZzyKeduNf_MKqRfZjP3kQ-QYV46-0W6S4_I39GNeHSLmTp1BtIR9h3ZtrBz7s733bZR5Ab9faTq4d3iHoWH0v7yn1ktI', 
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

    sendNotification(product,oldAmount,newAmount,expireDate,userId){

        
        //var notificationBody="";
       /*  if(newAmount>oldAmount)
            notificationBody+="The amount of "+product.name+" is increased. ";
        else
            notificationBody+="The amount of "+product.name+" is decreased. "; 
        notificationBody+="you have now " +newAmount+"\n";
 */
       



         User.findOne({ where: {id: userId }}).then(userObj => {
                if(userObj!=null)
                {
                    var notificationTitle="";
                    var notificationBody="";

                    if(newAmount<product.threshold & newAmount>10){
                         notificationTitle="Amount of " +product.name +" is changed!";
                         notificationBody=product.name +" is about to be empty!!";
                    }else if(newAmount<10 && userObj.productOutNoti==1){
                         notificationTitle=product.name + " taken out from Fridge";
                         notificationBody= product.name + " is ot of stock!!";
                    }
                    else{
                        return true;
                    }

                    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
                        to: userObj.token,
                        notification: {
                            title: notificationTitle, 
                            body: notificationBody
                        },
                        
                        data: {  //you can send only notification or only data(or include both)
                            my_key: 'my value',
                            my_another_key: 'my another value'
                        }
                    };
                    
            
                    return fcm.send(message, function(err, response){
                        if (err) {
                            console.log(err);
                            false;
                
                        } else {
                            console.log("Successfully sent with response: ", response);
                            true;
                
                        }
                    });
                }
           
     
        });
         


            


    }
  


    

};
