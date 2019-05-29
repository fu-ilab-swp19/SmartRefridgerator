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
  app.post('/api/product',productController.defineProduct);

  //user api
  app.post('/api/user',userController.registerUser);
  app.post('/api/user/authenticate',userController.authenticate);

  //notification api
  app.get('/api/notification',notificationController.test);

};