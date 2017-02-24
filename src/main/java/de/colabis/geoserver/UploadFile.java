package de.colabis.geoserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/upload")
public class UploadFile
{

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(){
        return "Everything seems to work!";
    }

    @POST
    @Path("/file")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response uploadPdfFile(  @FormDataParam("file") InputStream fileInputStream,
                                    @FormDataParam("file") FormDataContentDisposition fileMetaData,
                                    @FormDataParam("foldername") String foldername) throws Exception
    {
        String UPLOAD_PATH = "/var/tmp/GeoserverUpload/"+foldername+"/";
        try
        {
            String filename = fileMetaData.getFileName();
            if (filename.contains("/")){
                filename = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
            }
            if (filename.contains("\\")){
                filename = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
            }
            int read = 0;
            byte[] bytes = new byte[1024];

            Files.createDirectories(Paths.get(UPLOAD_PATH));
            OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + filename));
            while ((read = fileInputStream.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e)
        {
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }
        return Response.ok("Data uploaded successfully !!").build();
    }
}
