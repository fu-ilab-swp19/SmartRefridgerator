const User =require('../models').User;

module.exports={

    registerUser(req, res) {

        return User.findOne({ where: {email: req.body.email} }).then(user => {
           
            if(user==null)
            {
                User.create({
                    firstName:req.body.firstName,
                    lastName:req.body.lastName,
                    password:req.body.password,
                    email:req.body.email,
                    token:req.body.token
        
                }).then(

                    res.json( {result:"success",msg:'User is created successfully'})
                );

            }else{
                res.json( {result:"failed",msg:'Failed to register, Email is already used'});
            }

        });

    
      
    
    },
    authenticate(req,res){

         return User.findOne({ where: {email: req.body.email,password:req.body.password} }).then(userObj => {
            if(userObj==null)
            {
                res.json( {result:"failed",msg:'Failed to login, email or password is wrong'});
            }
            else
            {
                var finalResult={result:"success",user:userObj};
                res.json(finalResult);
            }
 
        });
     

    },


   
    

};
