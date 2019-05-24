//const Project = require('../models').Project;
//const Test = require('../models').Test;
//const TestDetail = require('../models').TestDetail;

module.exports={

    getTestsOfProject(req, res) {


        
        /* return Test.findAll(
            {
                 where: {projectId: req.params.projectId} 
        })
        .then(tests => res.json(tests));
     */
    },

    submitModuleTest(req, res) {

        /* console.log(req.body.checks);
        return Test.create({
        module:req.body.module,
        label:req.body.label,
        scr:req.body.scr,
        ddRevision:req.body.ddRevision,
        projectId:req.body.project
    
      }).then(res.json({result:'success'})); */
    
    },

       

};
