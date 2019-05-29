'use strict';
module.exports = (sequelize, DataTypes) => {
  const Product = sequelize.define('Product', {
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: false
    },
    name: DataTypes.STRING,
    description: DataTypes.STRING,
    brand: DataTypes.STRING,
    threshold: DataTypes.STRING,
    image: DataTypes.STRING   




  }, {});
   Product.associate = function(models) {
    // associations can be defined here
    Product.hasMany(models.ProductUser,{
      forigenKey:'productId',
      as:'productUsers'
    });
  }; 
  return Product;
};