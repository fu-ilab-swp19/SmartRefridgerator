'use strict';


const productController=require('../controllers').productController
const notificationController=require('../controllers').notificationController
const userController=require('../controllers').userController
const buylistController=require('../controllers').buylistController

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
  app.get('/api/user/productOutNoti/:userId',userController.manageProductOutNotification);
  app.post('/api/user/productOutNoti/:userId',userController.manageProductOutNotification);
  app.get('/api/user/statistics/:userId',userController.statistics);
  app.post('/api/user/token/:userId',userController.updateToken);
  //notification api
  app.get('/api/notification',notificationController.test);


  //buy list api
  app.post('/api/buylist/add',buylistController.addToBuylist);
  app.post('/api/buylist/delete',buylistController.deleteFromBuylist);
  app.get('/api/buylist/:userId',buylistController.getAllitems);


};