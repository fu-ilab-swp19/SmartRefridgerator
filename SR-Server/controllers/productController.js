//const Project = require('../models').Project;
//const Test = require('../models').Test;

const Product =require('../models').Product;
const ProductUser =require('../models').ProductUser;

const Sequelize = require('sequelize');
const op = Sequelize.Op;


module.exports={

    getAllProducts(req, res) {

    
       return Product.findAll().then(products => {
           //test
            res.json(products)

       });
    
    },

    getProduct(req,res){
        return Product.findOne({ where: {id:req.params.productId} }).then(prod => {
            if(prod==null)
            {
                res.json( {result:"failed",msg:'Product is not defined'});
            }
            else
            {
                var finalResult={result:"success",product:prod};
                res.json(finalResult);
            }
 
        });
    },
    defineProduct(req,res){


        return  Product.create({
            id:req.body.id,
            name:req.body.name,
            brand:req.body.brand,
            threshold:req.body.threshold,
            description:req.body.description

        }).then(res.json(

            {result:'success',msg:'product created successfully'}

        ));

    },

    addProductInFridge(req,res){
        

        return ProductUser.destroy({
            where: {
                UserId:req.body.userId,
                shelfNum:req.body.shelfNum
            }
        }).then(res.json(

                ProductUser.create({
                ProductId:req.body.productId,
                UserId:req.body.userId,
                shelfNum:req.body.shelfNum,
                expirationDate:req.body.expirationDate
    
            }).then(res.json(
    
                {result:'success',msg:'Product added successfully'}
    
            ))

        ));

        

    },
    updateProductInFridge(req,res){
    
        console.log(req.params); 

        return res.json({result:"sucess"});
    
    },
    getMyProducts(req,res){

        return ProductUser.findAll({
            include: [{ model: Product}],
            where:{amount:{ [op.ne]: null },UserId:req.params.userId}
        }).then(result => {

             var finalResult={result:"success",products:result};
             res.json(finalResult);
 
        });
    },
    getMyProductsNearExpire(req,res){

        var date = new Date();
        date.setDate(date.getDate() + 7);
        
        return ProductUser.findAll({
            include: [{ model: Product}],
            where:{

                amount:{ [op.ne]: null },
                expirationDate:{ [op.lte]: date },
                UserId:req.params.userId
            }
        }).then(result => {
            var finalResult={result:"success",products:result};
            res.json(finalResult);
 
        });

    },
    getMyProductsOutOfStock(req,res){

        
        return ProductUser.findAll({
            include: 
                { 
                    model: Product,as:'Product',
                    where:{threshold:{ [op.gt]: Sequelize.literal('amount')}  ,}
        
                 }
        
            ,
            where:
            {
                amount:{ [op.ne]: null },
                
                
                UserId:req.params.userId,

           }
        }).then(result => {
            var finalResult={result:"success",products:result};
            res.json(finalResult);
 
        });
   
    }


   
    

};
