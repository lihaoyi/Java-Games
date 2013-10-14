import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
class JCanvas extends JPanel{
        BufferedImage Picture;
        Graphics2D Painter;
        Graphics2D ClippedPainter;   
        int W;
        int H;
        int AX;
        int AY;
        Stroke DashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL, 0, new float[] {5, 5}, 0);
        Stroke DottedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] {1, 9}, 0);
        Stroke NormalStroke = new BasicStroke();
        public void setStroke(BasicStroke Target){
            Painter.setStroke(Target);
        }
        public void setBounds(int x, int y, int w, int h){
            super.setBounds(x, y, w, h);
            AX = x;
            AY = y;
            W = w;
            H = h;
            
        }
        public void init(){
            W = this.getBounds().width;
            H = this.getBounds().height;
            AX = this.getBounds().x;
            AY = this.getBounds().y;
            Picture = new BufferedImage(W, H, BufferedImage.TYPE_BYTE_INDEXED);
            Painter = Picture.createGraphics();
            ClippedPainter = Picture.createGraphics();
            
        }
        public void updatePicture(){
            Rectangle Bounds = this.getBounds();
        //    System.out.println(Bounds);
            Picture = new BufferedImage(Bounds.width, Bounds.height, GUI.TankImageType);
            AX = Bounds.x;
            AY = Bounds.y;
            
            Painter = Picture.createGraphics();
            
        }
        public void printLine(int INTX, int INTY, String Text, int Font, Color FontColor, char Alignment){
            
            Font FONT = Painter.getFont();
            Painter.setFont(FONT.deriveFont((float)Font));
            FontMetrics Metrics = Painter.getFontMetrics();
            Painter.setColor(FontColor);
            switch(Alignment){
                case 'c':
                    Painter.drawString(Text, INTX - Metrics.stringWidth(Text) / 2, INTY - Metrics.getHeight() / 2);
                    break;
                case 't':
                    Painter.drawString(Text, INTX - Metrics.stringWidth(Text) / 2, INTY);
                    break;
                case 'b':
                    Painter.drawString(Text, INTX - Metrics.stringWidth(Text) / 2, INTY - Metrics.getHeight());
                    break;
                case 'l':
                    Painter.drawString(Text, INTX, INTY - Metrics.getHeight() / 2);
                    break;
                case 'r':
                    Painter.drawString(Text, INTX - Metrics.stringWidth(Text), INTY - Metrics.getHeight() / 2);
                    break;
                default:
                    System.out.println("LINE PRINTING ERROR");
                    System.exit(0);
                    break;
            }
            Painter.setFont(FONT);    
            
            
        }
        public void paintComponent(Graphics paint){
            try{
                
                //Paint.drawImage(Picture, null, 0, 0);
                paint.drawImage(Picture, AX, AY, W, H, Color.white, null);
            }catch(NullPointerException e){
                System.out.println(e);
            }
        }
    }
