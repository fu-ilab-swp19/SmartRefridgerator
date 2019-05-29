'use strict';


const productController=require('../controllers').productController
const notificationController=require('../controllers').notificationController
const userController=require('../controllers').userController

module.exports = (app)=>{
  app.get('/api', (req, res) => res.status(200).send({
    message: 'Welcome to iav.',
  }));
  
  /* app.get('/api/projects',projectController.getAll);

  //app.get('/api/tests/:projectId',testController.getTestsOfProject(req.params.projectId));

  app.get('/api/tests/:projectId',testController.getTestsOfProject);


  app.post('/api/test',testController.submitModuleTest); */

  app.get('/api/products',productController.getAllProducts);

  app.post('/api/product',productController.defineProduct);

  app.get('/api/notification',notificationController.test);

  app.post('/api/user',userController.registerUser);
  app.get('/api/user',userController.authenticate);

};