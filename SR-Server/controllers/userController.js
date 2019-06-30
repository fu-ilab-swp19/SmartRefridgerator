const User = require('../models').User;
const ProductUser = require('../models').ProductUser;
const Buylist= require('../models').Buylist;
const Product= require('../models').Product;
const Sequelize = require('sequelize');
const op = Sequelize.Op;
module.exports = {

    registerUser(req, res) {

        return User.findOne({
            where: {
                email: req.body.email
            }
        }).then(user => {

            if (user == null) {
                User.create({
                    firstName: req.body.firstName,
                    lastName: req.body.lastName,
                    password: req.body.password,
                    email: req.body.email,
                    token: req.body.token,
                    productOutNoti: 0

                }).then(

                    res.json({
                        result: "success",
                        msg: 'User is created successfully'
                    })
                );

            } else {
                res.json({
                    result: "failed",
                    msg: 'Failed to register, Email is already used'
                });
            }

        });




    },
    authenticate(req, res) {

        return User.findOne({
            where: {
                email: req.body.email,
                password: req.body.password
            }
        }).then(userObj => {
            if (userObj == null) {
                res.json({
                    result: "failed",
                    msg: 'Failed to login, email or password is wrong'
                });
            } else {
                var finalResult = {
                    result: "success",
                    user: userObj
                };
                res.json(finalResult);
            }

        });


    },
    manageProductOutNotification(req, res) {

        console.log(req.method);
        if (req.method == "GET") {

            return User.findOne({
                where: {
                    id: req.params.userId
                }
            }).then(userObj => {
                if (userObj == null) {
                    res.json({
                        result: "failed",
                        msg: 'USer not existed'
                    });
                } else {
                    var finalResult = {
                        result: "success",
                        user: userObj
                    };
                    res.json(finalResult);
                }

            });

        } else if (req.method == "POST") {

            return User.update({
                    productOutNoti: req.body.productOutNoti
                }, {
                    where: {
                        id: req.params.userId
                    }
                })
                .then(result => {

                    res.json({
                        result: "success",
                        msg: "Settings updated successfully"
                    });
                });


        }

    },
    updateToken(req,res){
        return User.update({
            token: req.body.token
        }, {
            where: {
                id: req.params.userId
            }
        })
        .then(result => {

            res.json({
                result: "success",
                msg: "token updated successfully"
            });
        });

    },

    statistics(req, res) {
        var allProducts = 0;
        var OutOfStockProducts = 0;
        var NearExpireProducts = 0;
        var buyListProducts = 0;

        // number of all products
        ProductUser.count({

                where: {
                    UserId: req.params.userId
                },
                distinct: true,
                col: 'id'
            })
            .then(function(allProductsCount) {
                allProducts = allProductsCount;
                /////////////////////////////////////
                var date = new Date();
                date.setDate(date.getDate() + 7);
                ProductUser.count({
                    include: [{
                        model: Product
                    }],
                    where: {

                        amount: {
                            [op.ne]: null
                        },
                        expirationDate: {
                            [op.lte]: date
                        },
                        UserId: req.params.userId
                    },
                    distinct: true,
                    col: 'id'
                }).then(function(nearExpireProductsCount) {
                    NearExpireProducts = nearExpireProductsCount;
                    /////////////////////////////////////////

                    ProductUser.count({
                        include: {
                            model: Product,
                            as: 'Product',
                            where: {
                                threshold: {
                                    [op.gt]: Sequelize.literal('amount')
                                },
                            }

                        }

                        ,
                        where: {
                            amount: {
                                [op.ne]: null
                            },


                            UserId: req.params.userId,

                        },
                        distinct: true,
                        col: 'id'
                    }).then(function(OutOfStockProductsCount) {
                        OutOfStockProducts = OutOfStockProductsCount;
                        ///////////////////
                        Buylist.count({ 
                            include: [{ model: Product}],
                            where: {UserId:req.params.userId}
                        
                        }).then(function(buyListProductsCount) {
                            //test
                             buyListProducts=buyListProductsCount;
                             var result={all:allProducts,out:OutOfStockProducts,expired:NearExpireProducts,buylist:buyListProducts};
                             return  res.json(result)
                 
                        });


                        ////////////////

                    });


                    ///////////////////////////////////////

                });
                //////////////////////////////////////
            });


    }




};