package com.example.bruno.debarrio.entidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnviarMail extends AsyncTask<Void,Void,Void>{

    private Context context;
    private Session session;

    private String mail;
    private String subject;
    private String message;

    private ProgressDialog progressDialog;


    public EnviarMail(Context context,String mail, String subject, String message){
        this.context=context;
        this.mail=mail;
        this.subject=subject;
        this.message=message;
    }


    @Override
    protected Void doInBackground(Void... voids) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("appreclamosbarriales@gmail.com", "reclamos1234");
            }
        });

        try{
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress("testfrom354@gmail.com"));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
            mensaje.setSubject(subject);
            mensaje.setContent(message, "text/html; charset=utf-8");
            Transport.send(mensaje);

        } catch(MessagingException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog= ProgressDialog.show(context, "Enviando mail", "Por favor espere un momento...",false,false);

    }

    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "Mail enviado con exito", Toast.LENGTH_LONG).show();

    }
}

