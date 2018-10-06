import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class match extends JFrame
{
	int i,click=0,scr=0;
	String str="",strg="*";
	JButton[] icbutton=new JButton[8];
	JButton clkbutton;
	String s[]={"s1.jpg","s3.jpg","s2.jpg","s4.jpg","s3.jpg","s2.jpg","s1.jpg","s4.jpg"};
	ImageIcon[] icons=new ImageIcon[8];
	ImageIcon img;
	public match()
	{
		setLayout(new GridLayout(2,4));
		for(i=0;i<icbutton.length;i++)
		{
			icbutton[i]=new JButton();
			icons[i]=new ImageIcon(s[i]);
			icbutton[i].addActionListener(new buttonimg());
			add(icbutton[i]);
		}
	}
	public class buttonimg implements ActionListener
	{
		public void actionPerformed(ActionEvent ae)
		{
			for(int j=0;j<8;j++)
			{
				img=icons[j];
				clkbutton=icbutton[j];
				if(ae.getSource()==icbutton[j])
				{
					if((click>=8)&&(click<=16))
					{
						img=new ImageIcon("default.jpg");
						clkbutton.setIcon(img);
						click++;
					}
					else
					{
						img=icons[j];
						clkbutton.setIcon(img);
						click++;
						if(click>16&&(click%2==0))
						{
							strg=s[j];
						}
						else
						{
							str=s[j];
						}
					}
				}
			}
			if(strg==str)
			{
				scr=scr+5;
				System.out.println(scr);
			}
		}
	}
	public static void main(String args[])
	{
		match g=new match();
		g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		g.setVisible(true);
		g.setSize(200,200);
		g.setTitle("Find Match");
	}
}




