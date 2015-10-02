package service;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.net.httpserver.HttpServer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import java.io.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


/**
 * Created by James on 9/29/2015.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/fileService")
public class serverCalls {
    // The Java method will process HTTP GET requests
    @Path("/getList/{username}")
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces(MediaType.APPLICATION_JSON)
    public FileList getFileList(@PathParam("username") String username) {
        // Return some cliched textual content
        System.out.println("Sending files");

        FileList fileList = new FileList(username);
        return fileList;
    }

    @Path("/getFile/{username}/{fileName}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("username") String username, @PathParam("fileName") String fileName) {
        File file = new File("./fileSystem/users/" +  username + "/Files/" + fileName); // Initialize this to the File path you want to serve.
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .build();
    }

    @Path("/uploadFile/{username}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @PathParam("username") String username) {

        String uploadedFileLocation = ".fileSystem/users/" + username + "/Files" + fileDetail.getFileName();

        // save it
        saveFile(uploadedInputStream, uploadedFileLocation);

        String output = "File uploaded via Jersey based RESTFul Webservice to: " + uploadedFileLocation;

        return Response.status(200).entity(output).build();

    }



    private void saveFile(InputStream uploadedInputStream, String serverLocation) {
        try {
            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
            int read = 0;
            byte[] bytes = new byte[1024];
            outpuStream = new FileOutputStream(new File(serverLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Path("/addFile/{username}/{fileName}")
    @GET
    public Response addFile(@PathParam("fileName") String fileName, @PathParam("username") String username)
    {
        System.out.println("Receiving files");
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter("./fileSystem/users/" + username + "/fileList.txt", true));
            String line = fileName + "\n";
            output.append(line);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(200).entity("File Added").build();
    }

    @Path("/login/{username}/{password}")
    @GET
    public Response login(@PathParam("username") String username, @PathParam("password") String password)
    {
        File file = new File("./fileSystem/users/" + username);
        if (file.isDirectory())
        {
            System.out.println("Found user");
            file = new File("./fileSystem/users/" + username + "/Files");
            if (!file.isDirectory())
            {
                return Response.status(200).entity("Unconfirmed").build();
            }
            try {
                BufferedReader output = new BufferedReader(new FileReader("./fileSystem/users/" + username + "/userInfo.txt"));
                if (output.readLine().equals(password)) {
                    return Response.status(200).entity("Successful").build();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Response.status(200).entity("Unsuccessful").build();
    }

    @Path("/confirmUser/{username}")
    @GET
    public Response confirmUser(@PathParam("username") String username)
    {
        System.out.println("Confirming User");
        try {
            File dir = new File("fileSystem/users/" + username + "/Files");
            dir.mkdir();
            PrintWriter writer = new PrintWriter("fileSystem/users/" + username + "/fileList.txt", "UTF-8");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(200).entity("Account confirmed!").build();
    }

    @Path("/registerUser/{username}/{password}/{firstName}/{lastName}/{email}")
    @GET
    public Response registerUser(@PathParam("username") String username, @PathParam("password") String password, @PathParam("firstName") String firstName,
                                 @PathParam("lastName") String lastName, @PathParam("email") String email)
    {
        System.out.println("Creating user");
        File dir = new File("fileSystem/users/" + username);
        dir.mkdir();
        try {
            PrintWriter writer = new PrintWriter("fileSystem/users/" + username + "/userInfo.txt", "UTF-8");
            writer.println(password);
            writer.println(firstName);
            writer.println(lastName);
            writer.println(email);
            writer.close();
            confirmUser(username, email);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.status(200).entity("User Added").build();
    }

    private void confirmUser(String userName, String email) {
        // Recipient's email ID needs to be mentioned.
        String to = email; //change accordingly

        // Sender's email ID needs to be mentioned
        String from = "fileserviceconfirmation@gmail.com";//change accordingly
        final String username = "fileserviceconfirmation";//change accordingly
        final String password = "AEIOU123";//change accordingly

        // Assuming you are sending email through relay.jangosmtp.net
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Account Confirmation");

            String location = "http://10.16.163.199:4000/fileService/confirmUser/" + userName;

            String html = "Please click <a href=\n" + location + "\n>here.</a>";

            // Now set the actual message
            message.setContent(html, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:4000/");
        server.start();

        System.out.println("Server running");
        System.out.println("Visit: http://localhost:4000/fileService");
        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }
}
