package portfolioBuilderGUI;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.ArrayList;

public class PieChartPanel extends JPanel
{
    private ArrayList<Asset> assets;

    public PieChartPanel(ArrayList<Asset> assets)
    {
        this.assets = assets;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        int startAngle = 0;

        Color[] colors =
        {
            Color.BLUE,
            Color.RED,
            Color.GREEN,
            Color.YELLOW,
            Color.ORANGE
        };

        int i = 0;

        for(Asset asset : assets)
        {
            int angle =
                    (int)(asset.getAllocationPercentage() * 3.6);

            g2.setColor(
                    colors[i % colors.length]);

            g2.fillArc(
                    20,
                    20,
                    250,
                    250,
                    startAngle,
                    angle);

            startAngle += angle;

            i++;
        }
    }
}
