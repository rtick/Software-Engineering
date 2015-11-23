package service;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.net.httpserver.HttpServer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;

import javax.print.DocFlavor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;

import org.apache.commons.io.*;

import java.nio.file.Files;
import javax.ws.rs.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    public Response getFileList(@PathParam("username") String username) {
        // Return some cliched textual content
        System.out.println("Sending files");
        ArrayList<ArrayList<ArrayList<String>>> list = new ArrayList<ArrayList<ArrayList<String>>>();
        File dir = new File("./fileSystem/users/" +  username + "/Files/Encrypted");
        File[] directoryListing = dir.listFiles();
        ArrayList<ArrayList<String>> tempList1 =  new ArrayList<ArrayList<String>>();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                ArrayList<String> tempList2 =  new ArrayList<String>();
                tempList2.add(child.getName());
                tempList2.add(Long.toString((long)Math.ceil(child.length() * 0.001)));
                tempList1.add(tempList2);
            }
        }
        list.add(tempList1);
        dir = new File("./fileSystem/users/" +  username + "/Files/Decrypted");
        directoryListing = dir.listFiles();
        tempList1 =  new ArrayList<ArrayList<String>>();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                ArrayList<String> tempList2 =  new ArrayList<String>();
                tempList2.add(child.getName());
                tempList2.add(Long.toString((long)Math.ceil(child.length() * 0.001)));
                tempList1.add(tempList2);
            }
        }
        list.add(tempList1);
        dir = new File("./fileSystem/users/" +  username + "/SharedFiles");
        directoryListing = dir.listFiles();
        tempList1 =  new ArrayList<ArrayList<String>>();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                ArrayList<String> tempList2 =  new ArrayList<String>();
                tempList2.add(child.getName());
                tempList2.add(Long.toString((long)Math.ceil(child.length() * 0.001)));
                tempList1.add(tempList2);
            }
        }
        list.add(tempList1);
        return Response.ok()
                .entity(list)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/getUsers")
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserList() {
        // Return some cliched textual content
        System.out.println("Sending files");
        ArrayList<String> list = new ArrayList<String>();
        File dir = new File("./fileSystem/users/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                list.add(child.getName());
            }
        }
        return Response.ok()
                .entity(list)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/checkUsername/{username}")
    @GET
    public Response checkUsername(@PathParam("username") String username) {
        // Return some cliched textual content
        System.out.println("Checking name");
        File dir = new File("./fileSystem/users/");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.getName().equals(username)) {
                    System.out.println("test");
                    return Response.ok()
                            .entity("Successful")
                            .header("Access-Control-Allow-Origin", "*")
                            .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                            .build();
                }
            }
        }
        return Response.ok()
                .entity("Unsuccessful")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/getEncryptedFile/{username}/{fileName}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getEncryptedFile(@PathParam("username") String username, @PathParam("fileName") String fileName) {
        File file = new File("./fileSystem/users/" +  username + "/Files/Encrypted/" + fileName); // Initialize this to the File path you want to serve.
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/getDecryptedFile/{username}/{fileName}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getDecryptedFile(@PathParam("username") String username, @PathParam("fileName") String fileName) {
        File file = new File("./fileSystem/users/" +  username + "/Files/Decrypted/" + fileName); // Initialize this to the File path you want to serve.
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/getSharedFile/{username}/{fileName}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getSharedFile(@PathParam("username") String username, @PathParam("fileName") String fileName) {
        File file = new File("./fileSystem/users/" +  username + "/SharedFiles/" + fileName); // Initialize this to the File path you want to serve.
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/deleteEncryptedFile/{username}/{fileName}")
    @GET
    public Response deleteEncryptedFile(@PathParam("username") String username, @PathParam("fileName") String fileName) {
        File file = new File("./fileSystem/users/" +  username + "/Files/Encrypted/" + fileName); // Initialize this to the File path you want to serve.
        file.delete();
        System.out.println("./fileSystem/users/" +  username + "/Files/Encrypted/" + fileName);
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            System.out.println("got interrupted!");
        }        return Response.ok()
                .entity("File Deleted")
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/deleteDecryptedFile/{username}/{fileName}")
    @GET
    public Response deleteDecryptedFile(@PathParam("username") String username, @PathParam("fileName") String fileName) {
        File file = new File("./fileSystem/users/" +  username + "/Files/Decrypted/" + fileName); // Initialize this to the File path you want to serve.
        file.delete();
        System.out.println("./fileSystem/users/" +  username + "/Files/Decrypted/" + fileName);
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            System.out.println("got interrupted!");
        }        return Response.ok()
                .entity("File Deleted")
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/deleteSharedFile/{username}/{fileName}")
    @GET
    public Response deleteSharedFile(@PathParam("username") String username, @PathParam("fileName") String fileName) {
        File file = new File("./fileSystem/users/" +  username + "/SharedFiles/" + fileName); // Initialize this to the File path you want to serve.
        file.delete();
        System.out.println("./fileSystem/users/" +  username + "/SharedFiles/" + fileName);
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){
            System.out.println("got interrupted!");
        }        return Response.ok()
                .entity("File Deleted")
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/uploadEncryptedText/{username}/{ip}/{name}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadEncryptedText(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @PathParam("username") String username,
            @PathParam("ip") String ip,
            @PathParam("name") String name) {
        String uploadedFileLocation = "./fileSystem/users/" + username + "/Files/Encrypted/" + name;
            File file = new File(uploadedFileLocation);
        System.out.println(file.exists());
        if (file.exists())
            {
                return Response.ok()
                        .entity("window.open('http://" + ip + ":8080/main.html', '_self');window.alert('File name exists');")
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .build();
            }
            System.gc();
        // save it
        System.out.println(fileDetail.getFileName());
        saveFile(uploadedInputStream, uploadedFileLocation);
        return Response.ok()
                .entity("window.open('http://" + ip + ":8080/main.html', '_self')")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/shareFile/{username}/{fileName}/{sharedUser}")
    @GET
    public Response shareFile(@PathParam("username") String username, @PathParam("fileName") String fileName, @PathParam("sharedUser") String sharedUser) {
        File shareFile = new File("./fileSystem/users/" +  username + "/Files/Decrypted/" + fileName); // Initialize this to the File path you want to serve.
        File dest = new File("./fileSystem/users/" +  sharedUser + "/SharedFiles/" + fileName);
        System.out.println("./fileSystem/users/" +  sharedUser + "/SharedFiles/" + fileName);
        try {
            FileUtils.copyFile(shareFile, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = new File("fileSystem/users/" + username + "/userInfo.txt");
        try {
            String email = (String) FileUtils.readLines(file).get(3);
            confirmShareEmail(username, email, sharedUser, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok()
                .entity("File Shared")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/shareFileConfirm/{username}/{fileName}/{sharedUser}/{ip}")
    @GET
    public Response shareFileConfirm(@PathParam("username") String username, @PathParam("fileName") String fileName, @PathParam("sharedUser") String sharedUser,@PathParam("ip") String ip ) {
        System.out.println("Creating user");
        File file = new File("fileSystem/users/" + sharedUser + "/userInfo.txt");
        try {
            String email = (String) FileUtils.readLines(file).get(3);
            String password = (String)FileUtils.readLines(file).get(0);
            shareFileEmail(username, email, sharedUser, fileName, ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok()
                .entity("Reset")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/uploadFile/{username}/{ip}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @PathParam("username") String username,
            @PathParam("ip") String ip) {

        String uploadedFileLocation = "./fileSystem/users/" + username + "/Files/Decrypted/" + fileDetail.getFileName();
        File file = new File(uploadedFileLocation);
        if (file.exists())
        {
            return Response.ok()
                    .entity("<script>window.open('http://" + ip + ":8080/main.html', '_self');window.alert('File name exists');</script>")
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .build();
        }
        System.gc();
        // save it
        System.out.println(fileDetail.getFileName());
        saveFile(uploadedInputStream, uploadedFileLocation);

        return Response.ok()
                .entity("<script>window.open('http://" + ip + ":8080/main.html', '_self')</script>")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
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
            System.gc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @Path("/getName/{username}")
    @GET
    public Response getName(@PathParam("username") String username)
    {
        String returnVal = "";
        try {
            BufferedReader output = new BufferedReader(new FileReader("./fileSystem/users/" + username + "/userInfo.txt"));
            output.readLine();
            returnVal = output.readLine();
            System.gc();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok()
                .entity(returnVal)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/login/{username}/{password}")
    @GET
    public Response login(@PathParam("username") String username)
    {
        File file = new File("./fileSystem/users/" + username);
        if (file.isDirectory())
        {
            System.out.println("Found user");
            file = new File("./fileSystem/users/" + username + "/Files");
            if (!file.isDirectory())
            {
                return Response.ok()
                        .entity("Unconfirmed")
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .build();
            }
            try {
                BufferedReader output = new BufferedReader(new FileReader("./fileSystem/users/" + username + "/userInfo.txt"));
                String password = output.readLine();
                output.close();
                System.gc();
                System.out.println("test");
                return Response.ok()
                        .entity(password)
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .build();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Response.ok()
                .entity("Unsuccessful")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/confirmUser/{username}/{ip}")
    @GET
    @Produces("text/html")
    public Response confirmUser(@PathParam("username") String username,
                                @PathParam("ip") String ip)
    {
        System.out.println("Confirming User");
            File dir = new File("fileSystem/users/" + username + "/Files");
            dir.mkdir();
            dir = new File("fileSystem/users/" + username + "/Files/Encrypted");
            dir.mkdir();
            dir = new File("fileSystem/users/" + username + "/Files/Decrypted");
            dir.mkdir();
            dir = new File("fileSystem/users/" + username + "/SharedFiles");
            dir.mkdir();

        String location = "http://" + ip + ":8080/login.html";

        return Response.ok()
                .entity("Account Confirmed! Visit <a href=\n" + location + "\n>here</a> to login.")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/sendResetEmail/{username}/{ip}")
    @GET
    public Response sendResetEmail(@PathParam("username") String username, @PathParam("ip") String ip)
    {
        System.out.println("Creating user");
        File file = new File("fileSystem/users/" + username + "/userInfo.txt");
        try {
            String email = (String) FileUtils.readLines(file).get(3);
            String password = (String) FileUtils.readLines(file).get(0);
            System.out.println(email);
            resetUser(username, email, password, ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok()
                .entity("Reset")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/resetPass/{username}/{oldPassword}/{newPassword}")
    @GET
    public Response resetPass(@PathParam("username") String username, @PathParam("oldPassword") String oldPassword, @PathParam("newPassword") String newPassword)
    {
        System.out.println("Resetting user password");
        File file = new File("fileSystem/users/" + username + "/userInfo.txt");
        try {
            String password = (String) FileUtils.readLines(file).get(0);
            System.out.println(oldPassword);
            if (!password.equals(oldPassword))
            {
                return Response.ok()
                        .entity("Unsuccessful")
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .build();
            }
            String firstName = (String) FileUtils.readLines(file).get(1);
            String lastName = (String) FileUtils.readLines(file).get(2);
            String email = (String) FileUtils.readLines(file).get(3);
            PrintWriter writer = new PrintWriter(file);
            writer.println(newPassword);
            writer.println(firstName);
            writer.println(lastName);
            writer.println(email);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok()
                .entity("Successful")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    @Path("/registerUser/{username}/{password}/{firstName}/{lastName}/{email}/{ip}")
    @GET
    public Response registerUser(@PathParam("username") String username, @PathParam("password") String password, @PathParam("firstName") String firstName,
                                 @PathParam("lastName") String lastName, @PathParam("email") String email, @PathParam("ip") String ip)
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
            confirmUser(username, email, ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.ok()
                .entity("User Added")
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .build();
    }

    private void confirmUser(String userName, String email, String ip) {
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

            String location = "http://" + ip + ":4000/fileService/confirmUser/" + userName + "/" + ip;

            String html = "Thank you for creating a FileService account. Your username is: " + userName + "<br><br>Please confirm your account by visiting the " +
                    "link below: <a href=\n" + location + "\n>here</a><br><br>Clicking the link will register your account with us. It's fast and easy! " +
                    "If you cannot click the link, copy and paste the full URL into your web browser.<br><br>If you do not want to confirm your account, or this " +
                    "email was generated in error, please ignore this message.<br><br>Thanks!<br>FileService Security Team";

            // Now set the actual message
            message.setContent(html, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetUser(String userName, String email, String password, String ip) {
        // Recipient's email ID needs to be mentioned.
        String to = email; //change accordingly

        // Sender's email ID needs to be mentioned
        String from = "fileserviceconfirmation@gmail.com";//change accordingly
        final String username = "fileserviceconfirmation";//change accordingly
        final String emailPassword = "AEIOU123";//change accordingly

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
                        return new PasswordAuthentication(username, emailPassword);
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
            message.setSubject("Reset Password");
            System.out.println("2:" + password);
            String location = "http://" + ip + ":8080/tempReset.html?" + userName + ";" + password;

            String html = "Hello " + userName + "!<br><br>Please reset your password by visiting the " +
                    "link below: <a href=\n" + location + "\n>here</a><br><br>If you cannot click the link, copy and paste the full URL into your web browser." +
                    "<br><br>If you do not want to reset your password, or this email was generated in error, please ignore this message." +
                    "<br><br>Thanks!<br>FileService Security Team";

            // Now set the actual message
            message.setContent(html, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void shareFileEmail(String userName, String email, String sharedUser, String fileName, String ip) {
        // Recipient's email ID needs to be mentioned.
        String to = email; //change accordingly

        // Sender's email ID needs to be mentioned
        String from = "fileserviceconfirmation@gmail.com";//change accordingly
        final String username = "fileserviceconfirmation";//change accordingly
        final String emailPassword = "AEIOU123";//change accordingly

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
                        return new PasswordAuthentication(username, emailPassword);
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

            // Set Subject: header fields
            message.setSubject("File Received");
            String encodedFileName = fileName.replaceAll(" ", "%20");
            String location = "http://" + ip + ":4000/fileService/shareFile/" + userName + "/" + encodedFileName + "/" + sharedUser;
            System.out.println(location);

            String html = "Hello " + sharedUser + "!<br><br>" + userName + " has shared file \"" + fileName + "\" with you.<br><br>If you wish to receive this file click the " +
                    "link: <a href=\n" + location + "\n>here</a><br><br>" +
                    "If you cannot click the link, copy and paste the full URL into your web browser.<br><br>If you do not want to receive this file, or this " +
                    "email was generated in error, please ignore this message.<br><br>Thanks!<br>FileService Security Team";

            // Now set the actual message
            message.setContent(html, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmShareEmail(String userName, String email, String sharedUser, String fileName) {
        // Recipient's email ID needs to be mentioned.
        String to = email; //change accordingly

        // Sender's email ID needs to be mentioned
        String from = "fileserviceconfirmation@gmail.com";//change accordingly
        final String username = "fileserviceconfirmation";//change accordingly
        final String emailPassword = "AEIOU123";//change accordingly

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
                        return new PasswordAuthentication(username, emailPassword);
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

            // Set Subject: header fields
            message.setSubject("File Received");
            String encodedFileName = fileName.replaceAll(" ", "%20");

            String html = "Hello " + userName + "!<br><br>" + sharedUser + " has accepted your file \"" + fileName + "\".<br><br>If this email was generated in error, please ignore this message." +
                    "<br><br>Thanks!<br>FileService Security Team";

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
