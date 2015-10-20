'use strict';
var util = require('util'),
    path = require('path'),
    yeoman = require('yeoman-generator'),
    childProcess = require('child_process'),
    chalk = require('chalk'),
    _s = require('underscore.string'),
    scriptBase = require('../script-base');

var exec = childProcess.exec;
var spawn = childProcess.spawn;

var CloudFoundryProdGenerator = module.exports = function CloudFoundryProdGenerator() {
    yeoman.generators.Base.apply(this, arguments);
    console.log(chalk.bold('CloudFoundry configuration is starting'));
    this.env.options.appPath = this.config.get('appPath') || 'src/main/webapp';
    this.baseName = this.config.get('baseName');
    this.packageName = this.config.get('packageName');
    this.packageFolder = this.config.get('packageFolder');
    this.javaVersion = this.config.get('javaVersion');
    this.hibernateCache = this.config.get('hibernateCache');
    this.databaseType = this.config.get('databaseType');
    this.devDatabaseType = this.config.get('devDatabaseType');
    this.prodDatabaseType = this.config.get('prodDatabaseType');
    this.angularAppName = _s.camelize(_s.slugify(this.baseName)) + 'App';
};

util.inherits(CloudFoundryProdGenerator, yeoman.generators.Base);
util.inherits(CloudFoundryProdGenerator, scriptBase);

CloudFoundryProdGenerator.prototype.askForName = function askForName() {
    var done = this.async();
    var prompts = [{
        name: 'cloudfoundryDeployedName',
        message: 'Name to deploy as:',
        default: 'meetings'
    },
    {
        type: 'list',
        name: 'cloudfoundryProfile',
        message: 'Which profile would you like to use?',
        choices: [
            {
                value: 'dev',
                name: 'dev'
            },
            {
                value: 'prod',
                name: 'prod'
            }
        ],
        default: 0
    },
    {
        name: 'cloudfoundryDomainName',
        message: 'What is the domain to deploy the service too?',
        default: 'arterialeducation.com.au'
    },
    {
        name: 'cloudfoundryDatabaseName',
        message: 'What is the name of your database instance?',
        default: 'meetingdb'
    },
    {
        name: 'cloudfoundryDatabaseServiceName',
        message: 'What is the name of your database service?',
        default: 'cleardb'
    },
    {
        name: 'cloudfoundryDatabaseServicePlan',
        message: 'What is the name of your database plan?',
        default: 'amp'
    }];

    this.prompt(prompts, function (props) {
        this.cloudfoundryDeployedName = this._.slugify(props.cloudfoundryDeployedName).split('-').join('');
        this.cloudfoundryProfile = props.cloudfoundryProfile;
        this.cloudfoundryDomainName = props.cloudfoundryDomainName;
        this.cloudfoundryDatabaseName = props.cloudfoundryDatabaseName;
        this.cloudfoundryDatabaseServiceName = props.cloudfoundryDatabaseServiceName;
        this.cloudfoundryDatabaseServicePlan = props.cloudfoundryDatabaseServicePlan;
        done();
    }.bind(this));
};

CloudFoundryProdGenerator.prototype.copyCloudFoundryFiles = function copyCloudFoundryFiles() {
    if(this.abort) return;
    var done = this.async();
    this.log(chalk.bold('\nCreating Cloud Foundry deployment files'));

    this.template('_manifest-prod.yml', 'deploy/cloudfoundry/manifest-prod.yml');
    this.conflicter.resolve(function (err) {
        done();
    });
};

CloudFoundryProdGenerator.prototype.checkInstallation = function checkInstallation() {
    if(this.abort) return;
    var done = this.async();

    exec('cf --version', function (err) {
        if (err) {
            this.log.error('cloudfoundry\'s cf command line interface is not available. ' +
            'You can install it via https://github.com/cloudfoundry/cli/releases');
            this.abort = true;
        }
        done();
    }.bind(this));
};

CloudFoundryProdGenerator.prototype.cloudfoundryAppShow = function cloudfoundryAppShow() {
    if(this.abort || typeof this.dist_repo_url !== 'undefined') return;
    var done = this.async();

    this.log(chalk.bold("\nChecking for an existing Cloud Foundry hosting environment..."));
    var child = exec('cf app '+this.cloudfoundryDeployedName+' ', { }, function (err, stdout, stderr) {
        var lines = stdout.split('\n');
        var dist_repo = '';
        // Unauthenticated
        if (stdout.search('cf login') >= 0) {
            this.log.error('Error: Not authenticated. Run \'cf login\' to login to your cloudfoundry account and try again.');
            this.abort = true;
        }
        done();
    }.bind(this));
};

CloudFoundryProdGenerator.prototype.cloudfoundryAppCreate = function cloudfoundryAppCreate() {
    if(this.abort || typeof this.dist_repo_url !== 'undefined') return;
    var done = this.async();

    this.log(chalk.bold("\nCreating your Cloud Foundry hosting environment, this may take a couple minutes..."));
    var insight = this.insight();
    insight.track('generator', 'cloudfoundry');
    this.log(chalk.bold("Creating the database"));
    var child = exec('cf create-service ' + this.cloudfoundryDatabaseServiceName + ' ' + this.cloudfoundryDatabaseServicePlan + ' ' + this.cloudfoundryDatabaseName, { }, function (err, stdout, stderr) {
        done();
    }.bind(this));

    child.stdout.on('data', function(data) {
        this.log(data.toString());
    }.bind(this));
};

CloudFoundryProdGenerator.prototype.productionBuild = function productionBuild() {
    if(this.abort) return;
    var done = this.async();
    var mvn = 'mvn package -Pprod -DskipTests';
    if (this.cloudfoundryProfile == 'prod') {
        this.log(chalk.bold('\nBuilding the application with the production profile'));
    } else {
        this.log(chalk.bold('\nBuilding the application with the development profile'));
        mvn = 'mvn package -DskipTests';
    }
    var child = exec(mvn, function (err, stdout) {
        if (err) {
            this.log.error(err);
        }
        done();
    }.bind(this));

    child.stdout.on('data', function(data) {
        this.log(data.toString());
    }.bind(this));
};

CloudFoundryProdGenerator.prototype.cloudfoundryPush = function cloudfoundryPush() {
    if(this.abort) return;
    var done = this.async();

    this.log(chalk.bold('\nPushing the application to Cloud Foundry'));
    var child = exec('cf push -f ./deploy/cloudfoundry/manifest-prod.yml -p target/arterialedu-0.0.1-SNAPSHOT.war', function (err, stdout) {
        if (err) {
            this.log.error(err);
        }
        done();
    }.bind(this));

    child.stdout.on('data', function(data) {
        this.log(data.toString());
    }.bind(this));
};

CloudFoundryProdGenerator.prototype.restartApp = function restartApp() {
    if(this.abort || !this.cloudfoundry_remote_exists ) return;
    this.log(chalk.bold("\nRestarting your cloudfoundry app.\n"));

    var child = exec('cf restart ' + this.cloudfoundryDeployedName, function(err, stdout, stderr) {
        this.log(chalk.green('\nYour app should now be live'));
        if(hasWarning) {
            this.log(chalk.green('\nYou may need to address the issues mentioned above and restart the server for the app to work correctly \n\t' +
            'rhc app-restart -a ' + this.cloudfoundryDeployedName));
        }
        this.log(chalk.yellow('After application modification, re-deploy it with\n\t' + chalk.bold('grunt deploycloudfoundry')));
    }.bind(this));
};
