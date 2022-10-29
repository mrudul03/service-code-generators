module.exports = (sequelize, Sequelize) => {
  
  const Email = sequelize.define("email", {
    
    type: {
      type: Sequelize.STRING
    },
    
    
    emailaddress: {
      type: Sequelize.STRING
    },
    
  });
  return Email;
};
