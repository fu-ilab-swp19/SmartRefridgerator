const Buylist =require('../models').Buylist;
const Product =require('../models').Product;

module.exports={

    getAllitems(req, res) {


        return Buylist.findAll({ 
            include: [{ model: Product}],
            where: {UserId:req.params.userId}
        
        }).then(objects => {
            //test
             var result={result:"success",items:objects};
             res.json(result)
 
        });

    
    },

    addToBuylist(req, res) {

        Buylist.findOne({ 
            where: {UserId:req.body.userId,
                ProductId:req.body.productId,}
        
        }).then(object => {

            if(object==null){
                return Buylist.create({
                    UserId:req.body.userId,
                    ProductId:req.body.productId,
                    reminder:req.body.reminder
        
                }).then(
        
                    res.json( {result:"success",msg:'Item added successfully to Buylist'})
                );

            }else{
                res.json( {result:"success",msg:'Item is already in Buylist'})
            }

        });


     

    
    },
    deleteFromBuylist(req, res) {

        return Buylist.destroy({
            where: {
                UserId:req.body.userId,
                ProductId:req.body.productId
            }
        }).then(
            res.json( {result:"success",msg:'Item removed successfully from Buylist'})
        );
    }


   
    

};
