# Movie Lookup Application

## Running the Application

### System Requirments

- Gradle 4.4.x
- Node 10
- NPM 6.4.x

### Starting the Backend

After cloning the repository, navigate to the `webservice` folder and run the following command:

```bash
gradle run
```

The server is fully functional once the script is 83% loaded.

### Starting the Frontend

After cloning the repository, navigate to the `webapp` folder and run the following commands:

```bash
npm i
npm run serve
```

### Using the Application

Once the front and back ends have been started, navigate to [http://localhost:8080](http://localhost:8080) to use the application.

## Future versions

With future versions of this app, I would paginate the results rather than just showing the first ten. I would also make the intital search load faster.

## Additional Features

Some additional features I've included in this application are:

- A loading circle after a search has been submitted for better UX.
- Making it fully responsive.
- Making it accessible to screen readers with proper labels and alt-text.

## Screenshot

![Screenshot of app](screenshot.png?raw=true)
