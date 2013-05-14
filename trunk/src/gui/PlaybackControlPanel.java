package gui;

import ddf.minim.Playable;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Stefan Marks
 */
public class PlaybackControlPanel extends javax.swing.JPanel
{
    private static final int STEPS_PER_SECOND = 10;
    
    /**
     * Creates new form PlaybackControl
     */
    public PlaybackControlPanel()
    {
        initComponents();
        
        source = null;
        updateControls();
        
        TimerTask t = new TimerTask() {
            @Override
            public void run()
            {
                if ( (source != null) && source.isPlaying() )
                {
                    updateFromPlayback = true;
                    sldTime.setValue(source.position() * STEPS_PER_SECOND / 1000);
                    updateFromPlayback = false;
                } 
            }  
        };
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(t, 100, 100);        
        sldTime.addChangeListener(new SliderChangeListener());
    }

    public void attachToAudio(Playable playable)
    {
        this.source = playable;
        int len = source.length();
        sldTime.setMaximum(len * STEPS_PER_SECOND / 1000);
        sldTime.setValue(0);
        int idx = 0;
        while ( majTicks[idx] * 5 < len / 1000 ) { idx++; }
        sldTime.setMajorTickSpacing(majTicks[idx] * STEPS_PER_SECOND);
        sldTime.setMinorTickSpacing(minTicks[idx] * STEPS_PER_SECOND);
        int l = 0;
        Dictionary<Integer, JComponent> labels = new Hashtable<>();
        while ( l < len )
        {
            String  label = String.format("%d:%02d", (int) (l/(1000*60)), (l / 1000) % 60);
            Integer value = l * STEPS_PER_SECOND / 1000;
            labels.put(value, new JLabel(label));
            l += sldTime.getMajorTickSpacing() * 1000 / STEPS_PER_SECOND;
        }
        sldTime.setLabelTable(labels);
        updateControls();
    }
    
    public void detachFromAudio()
    {
        this.source = null;
        updateControls();
        sldTime.setValue(0);
    }
    
    private void updateControls()
    {
        btnRewind.setEnabled(source != null);
        btnPlay.setEnabled((source != null) && !source.isPlaying());
        btnStop.setEnabled((source != null) && source.isPlaying());
        sldTime.setEnabled(source != null);
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

        sldTime = new javax.swing.JSlider();
        btnRewind = new javax.swing.JButton();
        btnPlay = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        sldTime.setMajorTickSpacing(60);
        sldTime.setMinorTickSpacing(10);
        sldTime.setPaintLabels(true);
        sldTime.setPaintTicks(true);
        sldTime.setValue(0);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        add(sldTime, gridBagConstraints);

        btnRewind.setText("|<");
        btnRewind.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnRewindPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(btnRewind, gridBagConstraints);

        btnPlay.setText(">");
        btnPlay.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnPlayPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(btnPlay, gridBagConstraints);

        btnStop.setText("||");
        btnStop.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnStopPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(btnStop, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRewindPressed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnRewindPressed
    {//GEN-HEADEREND:event_btnRewindPressed
        sldTime.setValue(0);
        updateControls();
    }//GEN-LAST:event_btnRewindPressed

    private void btnPlayPressed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnPlayPressed
    {//GEN-HEADEREND:event_btnPlayPressed
        source.play(sldTime.getValue() * 1000 / STEPS_PER_SECOND);
        updateControls();
    }//GEN-LAST:event_btnPlayPressed

    private void btnStopPressed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnStopPressed
    {//GEN-HEADEREND:event_btnStopPressed
        source.pause();
        updateControls();
    }//GEN-LAST:event_btnStopPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnRewind;
    private javax.swing.JButton btnStop;
    private javax.swing.JSlider sldTime;
    // End of variables declaration//GEN-END:variables

    private class SliderChangeListener implements ChangeListener
    {
        @Override
        public void stateChanged(javax.swing.event.ChangeEvent evt)
        {
            if ( updateFromPlayback ) return;
            int pos = sldTime.getValue() * 1000 / STEPS_PER_SECOND;
            if ( source != null )
            {
                if ( source.isPlaying() ) source.play(pos);
                else                      source.skip(pos - source.position());
            }
        }
    }

    Playable source;
    Timer    updateTimer;
    boolean  updateFromPlayback; 
    
    final int[] majTicks = {10, 60, 120, 240, 300, 600, 1200};
    final int[] minTicks = { 1,  5,  10,  20,  30,  60,  120};
}
