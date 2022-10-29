module.exports = (sequelize, Sequelize) => {
  
  const Contact = sequelize.define("contact", {
    
    contactType: {
      type: Sequelize.STRING
    },
    
    
    contactNumber: {
      type: Sequelize.STRING
    },
    
  });
  return Contact;
};
