package contents;
import javazoom.jl.decoder.*;
import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class TurnMp3IntoWAV {
    public void processAudioFile(String fileUrl) {
        try {
            // Download the MP3 file
            File downloadedFile = downloadFile(fileUrl);

            // Check file extension
            if (!downloadedFile.getName().toLowerCase().endsWith(".mp3")) {
                System.out.println("The file is not an MP3. Exiting.");
                return;
            }

            // Convert MP3 to WAV
            File wavFile = convertMp3ToWav(downloadedFile);
            System.out.println("Conversion complete: " + wavFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File downloadFile(String fileUrl) throws IOException {
        System.out.println("Downloading file...");
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        File tempFile = File.createTempFile("audio", ".mp3");
        tempFile.deleteOnExit();

        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        System.out.println("File downloaded to: " + tempFile.getAbsolutePath());
        return tempFile;
    }

    private File convertMp3ToWav(File mp3File) throws Exception {
        System.out.println("Converting MP3 to WAV...");

        // Prepare the input stream for MP3 decoding
        InputStream mp3InputStream = new BufferedInputStream(new FileInputStream(mp3File));
        Bitstream bitstream = new Bitstream(mp3InputStream);
        Decoder decoder = new Decoder();

        // Prepare the output WAV file
        File wavFile = new File(mp3File.getParent(), "output.wav");
        AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false); // Standard PCM format
        try (AudioInputStream audioInputStream = new AudioInputStream(
                new ByteArrayInputStream(decodeMp3ToPcm(bitstream, decoder)),
                audioFormat,
                AudioSystem.NOT_SPECIFIED // Length unknown
        )) {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavFile);
        }

        return wavFile;
    }

    private byte[] decodeMp3ToPcm(Bitstream bitstream, Decoder decoder) throws Exception {
        ByteArrayOutputStream pcmOutputStream = new ByteArrayOutputStream();
        boolean done = false;

        while (!done) {
            Header header = bitstream.readFrame();
            if (header == null) {
                done = true;
                continue;
            }

            SampleBuffer sampleBuffer = (SampleBuffer) decoder.decodeFrame(header, bitstream);

            // Write PCM data to the output stream
            short[] pcmSamples = sampleBuffer.getBuffer();
            for (short sample : pcmSamples) {
                pcmOutputStream.write((sample & 0xFF)); // Low byte
                pcmOutputStream.write((sample >> 8) & 0xFF); // High byte
            }

            bitstream.closeFrame();
        }

        return pcmOutputStream.toByteArray();
    }


}
