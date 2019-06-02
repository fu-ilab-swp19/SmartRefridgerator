'use strict';


const productController=require('../controllers').productController
const notificationController=require('../controllers').notificationController
const userController=require('../controllers').userController

module.exports = (app)=>{
  app.get('/api', (req, res) => res.status(200).send({
    message: 'Welcome to Smart Refrigerator.',
  }));
  
 

  // product api
  app.get('/api/products',productController.getAllProducts);
  app.get('/api/product/:productId',productController.getProduct);
  app.post('/api/product',productController.defineProduct);
  app.post('/api/addproduct/mobile',productController.addProductInFridge);
  
  app.get('/api/myproducts/:userId',productController.getMyProducts);
  app.get('/api/myproducts/expire/:userId',productController.getMyProductsNearExpire);
  app.get('/api/myproducts/outstock/:userId',productController.getMyProductsOutOfStock);

  app.get('/api/addproduct/controller/:shelfId/:amount',productController.updateProductInFridge);

  //user api
  app.post('/api/user',userController.registerUser);
  app.post('/api/user/authenticate',userController.authenticate);

  //notification api
  app.get('/api/notification',notificationController.test);

};