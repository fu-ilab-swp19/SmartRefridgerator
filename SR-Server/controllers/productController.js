//const Project = require('../models').Project;
//const Test = require('../models').Test;

const Product =require('../models').Product;

module.exports={

    getAllProducts(req, res) {

    
       return Product.findAll().then(products => {
           //test
            res.json(products)

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


   
    

};
