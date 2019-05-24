//const Project = require('../models').Project;
//const Test = require('../models').Test;

const Product =require('../models').Product;

module.exports={

    getAllProducts(req, res) {

    
       return Product.findAll().then(products => {
           //
            res.json(products)

       });
    
    },
    defineProduct(req,res){


        return  Product.create({
            name:req.body.name,
            description:req.body.description,
            brand:req.body.brand,
            threshold:"dsdsdsdsd",

        }).then(res.json(

            {result:'success'}

        ));

    },


   
    

};
