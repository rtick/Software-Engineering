package service;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.net.httpserver.HttpServer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;

import java.io.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;


/**
 * Created by James on 9/29/2015.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/fileService")
public class serverCalls {
    // The Java method will process HTTP GET requests
    @Path("/getList")
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Produces(MediaType.APPLICATION_JSON)
    public FileList getFileList() {
        // Return some cliched textual content
        System.out.println("Sending files");

        FileList fileList = new FileList();
        return fileList;
    }

    @Path("/getFile/{fileName}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("fileName") String fileName) {
        File file = new File("src/service/Files" + fileName); // Initialize this to the File path you want to serve.
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .build();
    }

    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(FormDataMultiPart form) {
        System.out.println(form);
        FormDataBodyPart filePart = form.getField("file");
        ContentDisposition headerOfFilePart =  filePart.getContentDisposition();
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);
        String filePath = "src/service/Files" + headerOfFilePart.getFileName();
        saveFile(fileInputStream, filePath);
        String output = "File saved to server location using FormDataMultiPart : " + filePath;
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



    @Path("/addFile/{fileName}/{fileOwner}")
    @GET
    public void addFile(@PathParam("fileName") String fileName, @PathParam("fileOwner") String fileOwner)
    {
        System.out.println("Receiving files");
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter("src/service/Files.txt", true));
            String line = fileName + "," + fileOwner + "\n";
            output.append(line);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
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
