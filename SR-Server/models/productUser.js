'use strict';
module.exports = (sequelize, DataTypes) => {
  const ProductUser = sequelize.define('ProductUser', {
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    shelfNum: DataTypes.INTEGER,
    expairationDate: DataTypes.STRING,
    amount:DataTypes.INTEGER

  }, {});

  ProductUser.associate = function(models) {
    // associations can be defined here
    ProductUser.belongsTo(models.Product,{
      forigenKey:'productId'
    
    });

    ProductUser.belongsTo(models.User,{
      forigenKey:'userId'
    });
  };
 
  return ProductUser;
};