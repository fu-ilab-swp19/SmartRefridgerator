'use strict';
module.exports = (sequelize, DataTypes) => {
  const Buylist = sequelize.define('Buylist', {
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    reminder: DataTypes.DATE,


  }, {});

  Buylist.associate = function(models) {
    // associations can be defined here
    Buylist.belongsTo(models.Product,{
      forigenKey:'productId',
    
    });

    Buylist.belongsTo(models.User,{
      forigenKey:'userId'
    });
  };
 
  return Buylist;
};