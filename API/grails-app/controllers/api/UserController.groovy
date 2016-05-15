package api

import grails.converters.JSON
import static org.springframework.http.HttpStatus.*

class UserController {

    /**
    * This method returns the list of all unique users email addresses in descending order.
    * @HttpMethod GET
    */
    def index() {
      def criteria = User.createCriteria()
		  def userInstanceList = criteria.list {
		    projections {
		        distinct('email')
		    }
			order('email', 'desc')
		  }

      def users = [users: userInstanceList]
      render users as JSON
    }

    /**
    * This method returns data for a user found using the email address sent with the GET request.
    * @HttpMethod GET
    * @params id = the email address of the user
    */
    def show() {
      String email = params.id
      def user = [user: User.findByEmail(email)]
      if(user == null) {
          render status:NOT_FOUND
      }
      else {
          render user as JSON
      }
    }

    /**
    * This method creates a new User with valid data using the data sent with the POST request.
    * @HttpMethod POST
    */
    def create() {
      def data = request.JSON.user

      def name = [title: data.name.title, first: data.name.first, last: data.name.last]
      def location = [street: data.location.street, city: data.location.city, state: data.location.state, zip: data.location.zip]
      def picture = [large: data.picture.large, medium: data.picture.medium, thumbnail: data.picture.thumbnail]

      def user = new User(gender: data.gender,
  			name: name,
        location: location,
        email: data.email,
        username: data.username,
        password: data.password,
        salt: data.salt,
        md5: data.md5,
        sha1: data.sha1,
        sha256: data.sha256,
        registered: data.registered,
        dob: data.dob,
        phone: data.phone,
        cell: data.cell,
        PPS: data.PPS,
        picture: picture
        )

      user.validate()
    		if (user.hasErrors()) {
    			respond status: NOT_ACCEPTABLE
    			println user.errors
    			return
    		}

      user.save(flush:true)
      respond status: CREATED
    }

    /**
    * This method updates the data for a user found using the email address sent with the the PUT request.
    * @HttpMethod PUT
    * @params id = the email address of the user
    */
    def update(){
      String email = params.id
      def user = User.findByEmail(email)

      if(user == null) {
          render status:404
      }
      else {
        def data = request.JSON.user

        def name = [title: data.name.title, first: data.name.first, last: data.name.last]
        def location = [street: data.location.street, city: data.location.city, state: data.location.state, zip: data.location.zip]
        def picture = [large: data.picture.large, medium: data.picture.medium, thumbnail: data.picture.thumbnail]

          user.gender = data.gender
          user.name = name
          user.location = location
          user.email = data.email
          user.username = data.username
          user.password = data.password
          user.salt = data.salt
          user.md5 = data.md5
          user.sha1 = data.sha1
          user.sha256 = data.sha256
          user.registered = data.registered
          user.dob = data.dob
          user.phone = data.phone
          user.cell = data.cell
          user.PPS = data.PPS
          user.picture = picture

        user.validate()
          if (user.hasErrors()) {
            respond status: NOT_ACCEPTABLE
            println user.errors
            return
          }

        user.save(flush:true)
        respond status: OK
      }
    }

    /**
    * This method rdeletes the user found using the email address sent with the DELETE request.
    * @HttpMethod DELETE
    */
    def delete() {
      String email = params.id
      def user = [user: User.findByEmail(email)]

      if(user == null) {
          render status:NOT_FOUND
      }
      else {
          user.delete()
          respond status: OK
      }
    }

}
