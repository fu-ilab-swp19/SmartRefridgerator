var createError=require('http-errors');
var express = require('express');
var logger = require('morgan');
const bodyParser = require('body-parser');
// This will be our application entry. We'll setup our server here.

const app = express();
// Log requests to the console.
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
// Setup a default catch-all route that sends back a welcome message in JSON format.

var models=require("./models");

models.sequelize.sync().then(function(){
  console.log("Database looks fine");
}).catch(function(err){
  console.log(err,"something went wrong");
});



require('./routes')(app);


const http = require('http');
const port = parseInt(process.env.PORT, 10) || 3000;
app.set('port', port);
const server = http.createServer(app);
server.listen(port,"192.168.178.21");

module.exports = app;




