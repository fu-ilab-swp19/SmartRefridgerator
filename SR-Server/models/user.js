'use strict';
module.exports = (sequelize, DataTypes) => {
  const User = sequelize.define('User', {
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true
    },
    firstName: DataTypes.STRING,
    lastName: DataTypes.STRING,
    email: DataTypes.STRING,
    password: DataTypes.STRING,
    token: DataTypes.STRING,
    productOutNoti:DataTypes.INTEGER




  }, {});
   User.associate = function(models) {
    // associations can be defined here
    User.hasMany(models.ProductUser,{
      forigenKey:'userId',
      as:'productUsers'
    });
  };
  return User;
};