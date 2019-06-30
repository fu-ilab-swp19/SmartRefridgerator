//const Project = require('../models').Project;
//const Test = require('../models').Test;

const Product =require('../models').Product;
const ProductUser =require('../models').ProductUser;
const notificationController=require('./notificationController');
const Sequelize = require('sequelize');
const op = Sequelize.Op;
const datakick = require('datakick');


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
                datakick.item(req.params.productId).then(function(data) {
                    console.log(JSON.stringify(data));
                    return  Product.create({
                        id:req.params.productId,
                        name:data.name,
                        brand:data.brand_name,
                        threshold:200,
                        description:null
                    }).then(prod =>{

                        var finalResult={result:"success",product:prod};
                        res.json(finalResult);
                    });
                    

                  }).catch(function(error) {
                    res.json( {result:"failed",msg:'Product is not defined. Please define it before putting in Fridge'});
                  });
                
            }
            else
            {
                var finalResult={result:"success",product:prod};
                res.json(finalResult);
            }
 
        });
    },
    defineProduct(req,res){

        Product.findOne({ where: {id:req.body.id} }).then(prod => {
            if(prod==null)
            {
                    Product.create({
                    id:req.body.id,
                    name:req.body.name,
                    brand:req.body.brand,
                    threshold:req.body.threshold,
                    description:req.body.description
        
                }).then( pro =>{
                   return datakick.add(pro.id, {
                        name: pro.name,
                        brand_name: pro.brand
                      }).then(function(data) {
                        console.log(JSON.stringify(data));
                        res.json( {result:'success',msg:'product defined successfully'} )
                      }).catch(function(error) {
                        console.log(error.message);
                        res.json( {result:'success',msg:'product defined successfully'} )
                      });
                     
                });
        
            }
            else{
                return res.json( {result:'error',msg:'product already defined'} )
            }
        });


 
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
                expirationDate:req.body.expirationDate,
                amount:0
    
            }).then(res.json(
    
                {result:'success',msg:'Product added successfully'}
    
            ))

        ));

        

    },
    updateProductInFridge(req,res){

       var user=1;
       var oldAmount=0;
       var expireDate=null;

        ProductUser.findOne(
            
            {    include: [{ model: Product}],
                 where: {shelfNum:req.params.shelfId,UserId:user} }).then(obj => {
            if(obj!=null)
            {
                oldAmount=obj.amount;
                expireDate=obj.expirationDate;
          

                return  ProductUser.update(
                    { amount: req.params.amount},
                    { where: { shelfNum : req.params.shelfId,UserId:user} }
               )
               .then(result =>{
   
                    // send notification
                    notificationController.sendNotification(obj.Product,oldAmount,req.params.amount,obj.expirationDate,user);
                    res.json({result:"success"});
                   }
               );
            }
        });

     
    

        
    
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
