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

                    res.json( {result:'User is created successfully'})
                );

            }else{
                res.json( {result:'Failed to register, Email is already used'});
            }

        });

    
      
    
    },
    authenticate(req,res){

         return User.findOne({ where: {email: req.body.email,password:req.body.password} }).then(user => {
            if(user==null)
            {
                res.json( {result:'Failed to login, email or password is wrong'});
            }
            else
            {
                res.json(user);
            }
 
        });
     

    },


   
    

};
