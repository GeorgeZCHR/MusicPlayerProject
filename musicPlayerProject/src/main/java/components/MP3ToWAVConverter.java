package components;
import javazoom.jl.decoder.*;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.net.URL;

public class MP3ToWAVConverter {
    private static boolean isURL;

    /*public static void main(String[] args) {
        // Replace with your input MP3 file and desired output WAV file
        *//*String inputMP3 = "Win The Battle Win The War - Everet Almond.mp3";
        String outputWAV = "music/Win The Battle Win The War - Everet Almond.wav";

        try {
            convertMP3ToWAV(inputMP3, outputWAV);
            System.out.println("Conversion complete: " + outputWAV);
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
    }*/

    public static void convertMP3ToWAV(URL mp3FileURL, String wavFilePath) throws Exception {
        // Create an MP3 file input stream
        InputStream mp3Stream = mp3FileURL.openStream();

        // Decode the MP3 file using JLayer
        Bitstream bitstream = new Bitstream(mp3Stream);
        Decoder decoder = new Decoder();

        // Prepare the output WAV file
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,  // WAV format
                44100,                          // Sample rate (standard for audio)
                16,                             // Sample size (bits per sample)
                2,                              // Channels (stereo)
                4,                              // Frame size
                44100,                          // Frame rate
                false                           // Big-endian
        );
        try (AudioInputStream audioInputStream = new AudioInputStream(
                new MP3ToPCMInputStream(bitstream, decoder, audioFormat),
                audioFormat,
                AudioSystem.NOT_SPECIFIED // Length is unknown
        )) {
            // Write the WAV file
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(wavFilePath));
        }
    }

    public static void convertMP3ToWAV(String mp3FilePath, String wavFilePath) throws Exception {
        // Create an MP3 file input stream
        InputStream mp3Stream = new BufferedInputStream(new FileInputStream(mp3FilePath));

        // Decode the MP3 file using JLayer
        Bitstream bitstream = new Bitstream(mp3Stream);
        Decoder decoder = new Decoder();

        // Prepare the output WAV file
        AudioFormat audioFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,  // WAV format
                44100,                          // Sample rate (standard for audio)
                16,                             // Sample size (bits per sample)
                2,                              // Channels (stereo)
                4,                              // Frame size
                44100,                          // Frame rate
                false                           // Big-endian
        );
        try (AudioInputStream audioInputStream = new AudioInputStream(
                new MP3ToPCMInputStream(bitstream, decoder, audioFormat),
                audioFormat,
                AudioSystem.NOT_SPECIFIED // Length is unknown
        )) {
            // Write the WAV file
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File(wavFilePath));
        }
    }

    /**
     * Custom InputStream for decoding MP3 data to PCM
     */
    private static class MP3ToPCMInputStream extends InputStream {
        private final Bitstream bitstream;
        private final Decoder decoder;
        private final AudioFormat format;
        private SampleBuffer outputBuffer;

        public MP3ToPCMInputStream(Bitstream bitstream, Decoder decoder, AudioFormat format) {
            this.bitstream = bitstream;
            this.decoder = decoder;
            this.format = format;
        }

        @Override
        public int read() throws IOException {
            // Not used, as we override read(byte[], int, int)
            throw new UnsupportedOperationException();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (outputBuffer == null || outputBuffer.getBufferLength() <= 0) {
                try {
                    Header header = bitstream.readFrame();
                    if (header == null) {
                        return -1; // End of file
                    }
                    outputBuffer = (SampleBuffer) decoder.decodeFrame(header, bitstream);
                    bitstream.closeFrame();
                } catch (BitstreamException | DecoderException e) {
                    throw new IOException(e);
                }
            }

            byte[] pcmData = convertToPCM(outputBuffer);
            int bytesToRead = Math.min(len, pcmData.length);
            System.arraycopy(pcmData, 0, b, off, bytesToRead);

            // Clear the buffer after reading
            outputBuffer = null;
            return bytesToRead;
        }

        private byte[] convertToPCM(SampleBuffer buffer) {
            short[] samples = buffer.getBuffer();
            byte[] pcm = new byte[samples.length * 2];
            for (int i = 0; i < samples.length; i++) {
                pcm[2 * i] = (byte) (samples[i] & 0xFF); // Low byte
                pcm[2 * i + 1] = (byte) ((samples[i] >> 8) & 0xFF); // High byte
            }
            return pcm;
        }
    }
}