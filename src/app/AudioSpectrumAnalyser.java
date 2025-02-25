package app;

import gui.FrequencySpectrumHistoryPanel;
import gui.FrequencySpectrumRenderPanel;
import gui.PlaybackControlPanel;
import analyser.SpectrumAnalyser;
import output.FileSpectrumOutputModule;
import gui.WaveformRenderPanel;
import ddf.minim.*;
import gui.PreferencesDialog;
import java.awt.BorderLayout;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import output.OscOutputModule;
import output.OutputModule;
import processing.core.PApplet;

/**
 * Audio Spectrum Analyser Framework
 * 
 * @author Stefan Marks
 * @version 1.0 - 15.05.2013: Created
 * @version 1.2 - 15.06.2013: Added preferences dialog and network logger
 * @version 1.3 - 19.06.2013: Swapped Y axis for spectrum history
 *                            Introduced different colour schemes
 * 
 * TODO: 
 * - jpeg export
 * - jpeg import
 * 
 */
public class AudioSpectrumAnalyser extends javax.swing.JFrame
{
    private static final String VERSION_NO = "1.4";
    
    /**
     * Creates new form BeatAnalyzerApp
     */
    public AudioSpectrumAnalyser()
    {
        initComponents();
        setTitle("Audio Spectrum Analyser v" + VERSION_NO);
        
        analyser = new SpectrumAnalyser(200, 2048);
        
        outputModules = new LinkedList<>();
        fileOutput    = new FileSpectrumOutputModule(analyser);
        networkOutput = new OscOutputModule(analyser);
        outputModules.add(fileOutput);
        outputModules.add(networkOutput);
        
        //analyser.registerFeatureDetector(new SpikeFeatureDetector("Bass",  0, 50, 100, 10, 50));
        //analyser.registerFeatureDetector(new SpikeFeatureDetector("Snare", 1, 2000, 7000, 15, 100));
        //analyser.registerFeatureDetector(new SpikeFeatureDetector("Cymbal", 2, 7000, 10000, 20, 100));
        
        renderWaveform = new WaveformRenderPanel(analyser);
        pnlWaveform.add(renderWaveform);
        
        renderFrequencySpectrum = new FrequencySpectrumRenderPanel(analyser);
        pnlFrequencySpectrum.add(renderFrequencySpectrum);
        
        //renderFeatureHistory = new FeatureHistoryRenderPanel(analyser);
        //pnlFrequencyHistory.add(renderFeatureHistory, BorderLayout.NORTH);
        
        renderFrequencyHistory = new FrequencySpectrumHistoryPanel(analyser);
        pnlFrequencyHistory.add(renderFrequencyHistory, BorderLayout.CENTER);
        
        playbackControl = new PlaybackControlPanel();
        pnlPlaybackControls.add(playbackControl);
        
        PApplet p = new PApplet();
        minim = new Minim(p);
        sound = null;
        lineIn  = minim.getLineIn();
        lineIn.mute();
        
        prevAudioPath = new File(".");
     
        preferences = new PreferencesDialog(this);
        loadPreferences();
        
        setSize(1024, 800);
        setLocationRelativeTo(null);
        
        openSoundFile(new File("data/Loop 02 - Amplifier - One Great Summer 10s.wav"));
        //openSoundFile(new File("data/Sweep.wav"));
    }

    
    /**
     * Opens the file selection dialog for reading a sound file.
     */
    private void selectSoundFile()
    {
        JFileChooser jfc = new JFileChooser(prevAudioPath);
        int choice = jfc.showOpenDialog(this);
        if ( choice == JFileChooser.APPROVE_OPTION )
        {
            File file = jfc.getSelectedFile();
            prevAudioPath = file.getParentFile();
            openSoundFile(file);
        }
    }
    
    
    /**
     * Starts reading and analysing a sound file.
     * 
     * @param file  the file to read and analyse
     */
    private void openSoundFile(File file)
    {
        // close old file first?
        closeSoundFile();
        // open new file
        if ( file.exists() )
        {
            try
            {
                sound = minim.loadFile(file.getAbsolutePath());
                analyser.attachToAudio(sound);
                if ( sound instanceof Playable )
                {
                    playbackControl.attachToAudio((Playable) sound);
                }

                for ( OutputModule outputModule : outputModules )
                {
                    outputModule.audioFileOpened(file);
                }

                loadPreferences();
                menuFileClose.setEnabled(true);
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(
                    this, 
                    "Could not open the audio File!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    /**
     * Starts reading and analysing live input
     * 
     * @param 
     */
    private void openLiveInput()
    {
        // close old file first?
        closeSoundFile();
        
        try
        {
            sound = lineIn;
            analyser.attachToAudio(sound);
            loadPreferences();
            menuFileClose.setEnabled(true);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(
                this, 
                "Could not open Line Input!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    /**
     * Closes the active sound file.
     */
    private void closeSoundFile()
    {
        if ( sound != null )
        {
            analyser.detachFromAudio();
            playbackControl.detachFromAudio();
            
            for ( OutputModule outputModule : outputModules )
            {
                outputModule.audioFileClosed();
            }                    
            
            if ( sound instanceof Playable ) 
            {
                sound.close();
            }
            sound = null;
            
            menuFileClose.setEnabled(false);
        }
    }
    
    
    private void loadPreferences()
    {
        preferences.loadFileOutputSettings(fileOutput);
        preferences.loadOscOutputSettings(networkOutput);
        preferences.setColourMap(renderFrequencyHistory.getColourMap());
    }
    
    
    private void applyPreferences()
    {
        preferences.applyFileOutputSettings(fileOutput);
        preferences.applyOscOutputSettings(networkOutput);
        renderFrequencyHistory.setColourMap(preferences.getColourMap());
        renderFrequencySpectrum.setColourMap(preferences.getColourMap());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlWaveform = new javax.swing.JPanel();
        pnlFrequencySpectrum = new javax.swing.JPanel();
        pnlFrequencyHistory = new javax.swing.JPanel();
        pnlPlaybackControls = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu menuFile = new javax.swing.JMenu();
        javax.swing.JMenuItem menuLiveInput = new javax.swing.JMenuItem();
        javax.swing.JMenuItem menuFileOpen = new javax.swing.JMenuItem();
        menuFileClose = new javax.swing.JMenuItem();
        javax.swing.JMenuItem menuFileQuit = new javax.swing.JMenuItem();
        javax.swing.JMenu menuSettings = new javax.swing.JMenu();
        javax.swing.JMenuItem menuSettings_Preferences = new javax.swing.JMenuItem();
        javax.swing.JMenu menuHelp = new javax.swing.JMenu();
        javax.swing.JMenuItem menuHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Beat Analyser v1.0");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlWaveform.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Waveform"), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255))));
        pnlWaveform.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlWaveform, gridBagConstraints);

        pnlFrequencySpectrum.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Frequency Spectrum"), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255))));
        pnlFrequencySpectrum.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlFrequencySpectrum, gridBagConstraints);

        pnlFrequencyHistory.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Frequency Spectrum"), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255))));
        pnlFrequencyHistory.setLayout(new java.awt.BorderLayout(0, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(pnlFrequencyHistory, gridBagConstraints);

        pnlPlaybackControls.setBorder(javax.swing.BorderFactory.createTitledBorder("Playback Controls"));
        pnlPlaybackControls.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(pnlPlaybackControls, gridBagConstraints);

        menuFile.setText("File");

        menuLiveInput.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        menuLiveInput.setText("Open Live Input");
        menuLiveInput.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuOpenLiveInput(evt);
            }
        });
        menuFile.add(menuLiveInput);

        menuFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuFileOpen.setText("Open File");
        menuFileOpen.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                openSoundFile(evt);
            }
        });
        menuFile.add(menuFileOpen);

        menuFileClose.setText("Close File");
        menuFileClose.setEnabled(false);
        menuFileClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuFileCloseActionPerformed(evt);
            }
        });
        menuFile.add(menuFileClose);

        menuFileQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        menuFileQuit.setText("Quit");
        menuFileQuit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuQuitActionPerformed(evt);
            }
        });
        menuFile.add(menuFileQuit);

        menuBar.add(menuFile);

        menuSettings.setText("Settings");

        menuSettings_Preferences.setText("Preferences");
        menuSettings_Preferences.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuSettings_PreferencesActionPerformed(evt);
            }
        });
        menuSettings.add(menuSettings_Preferences);

        menuBar.add(menuSettings);

        menuHelp.setText("Help");

        menuHelpAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_MASK));
        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                menuHelpAboutActionPerformed(evt);
            }
        });
        menuHelp.add(menuHelpAbout);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void openSoundFile(java.awt.event.ActionEvent evt)//GEN-FIRST:event_openSoundFile
    {//GEN-HEADEREND:event_openSoundFile
        selectSoundFile();
    }//GEN-LAST:event_openSoundFile

    
    private void menuQuitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuQuitActionPerformed
    {//GEN-HEADEREND:event_menuQuitActionPerformed
        this.dispose();
        minim.stop();
        System.exit(0);
    }//GEN-LAST:event_menuQuitActionPerformed

    
    private void menuHelpAboutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuHelpAboutActionPerformed
    {//GEN-HEADEREND:event_menuHelpAboutActionPerformed
         JOptionPane.showMessageDialog(
            this,
            "Audio Spectrum Analyser v" + VERSION_NO + "\n\n" +
            "(C) 2013-2015 by Stefan Marks\n" + 
            "Auckland University of Technology, New Zealand");
    }//GEN-LAST:event_menuHelpAboutActionPerformed

    
    private void menuSettings_PreferencesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuSettings_PreferencesActionPerformed
    {//GEN-HEADEREND:event_menuSettings_PreferencesActionPerformed
        PreferencesDialog.UserChoice choice = preferences.showDialog();
        if ( choice == PreferencesDialog.UserChoice.ACCEPT )
        {
            applyPreferences();
        }            
        else
        {
            loadPreferences();
        }
    }//GEN-LAST:event_menuSettings_PreferencesActionPerformed

    
    private void menuFileCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuFileCloseActionPerformed
    {//GEN-HEADEREND:event_menuFileCloseActionPerformed
        closeSoundFile();
    }//GEN-LAST:event_menuFileCloseActionPerformed

    
    private void menuOpenLiveInput(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuOpenLiveInput
    {//GEN-HEADEREND:event_menuOpenLiveInput
        openLiveInput();
    }//GEN-LAST:event_menuOpenLiveInput

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        final AudioSpectrumAnalyser app = new AudioSpectrumAnalyser();
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                app.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuFileClose;
    private javax.swing.JPanel pnlFrequencyHistory;
    private javax.swing.JPanel pnlFrequencySpectrum;
    private javax.swing.JPanel pnlPlaybackControls;
    private javax.swing.JPanel pnlWaveform;
    // End of variables declaration//GEN-END:variables

    private final Minim                 minim;
    private       AudioSource           sound;
    private final AudioInput            lineIn;
    private final SpectrumAnalyser      analyser;
    private       File                  prevAudioPath;
    
    private final List<OutputModule>            outputModules;
    private final FileSpectrumOutputModule      fileOutput;
    private final OscOutputModule               networkOutput;
    
    private final PreferencesDialog             preferences;
    
    private final WaveformRenderPanel           renderWaveform;
    private final FrequencySpectrumRenderPanel  renderFrequencySpectrum;
    //private final FeatureHistoryRenderPanel     renderFeatureHistory;
    private final FrequencySpectrumHistoryPanel renderFrequencyHistory;
    private final PlaybackControlPanel          playbackControl;
}
