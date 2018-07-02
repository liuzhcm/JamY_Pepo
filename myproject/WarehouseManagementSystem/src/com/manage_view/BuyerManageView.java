package com.manage_view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

import com.other_view.BuyerDialog;
import com.other_view.GoodDialog;
import com.service.BuyerService;
import com.service.GoodService;
import com.view.MainFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class BuyerManageView extends JPanel implements ActionListener,KeyListener{
	//��������Ҫ�Ŀؼ�
	JPanel jp1,jp2;
	JLabel jl1;
	JTextField jtf;
	JButton jb1,jb2,jb3,jb4;
	
	public static JTable jt;
	JScrollPane jsp;
	//����ģ�� ���ֹ���һ��goodService jt
	public static BuyerService buyerService;
	public static void main(String[] args) {
		
	}	
	public BuyerManageView(){
		jl1=new JLabel("������ɹ�Ա����");
		jtf=new JTextField(20);
		jtf.addKeyListener(this);
		jb1=new JButton("��ѯ");
		jb1.addActionListener(this);
		jb2=new JButton("����");
		jb2.addActionListener(this);
		jb3=new JButton("�޸�");
		jb3.addActionListener(this);
		jb4=new JButton("ɾ��");
		jb4.addActionListener(this);
		
		//�ϲ���
		jp1=new JPanel(new FlowLayout(FlowLayout.CENTER));
		jp1.add(jl1);
		jp1.add(jtf);
		jp1.add(jb1);
		
		//�м䲿��
		buyerService=new BuyerService();
		String hql="from TbBuyer";
		String paramer[]={};
		buyerService.queryBuyer(hql, paramer);
		jt=new JTable(buyerService);
		// ����table���ݾ���
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(JLabel.CENTER);
//	    tcr.setHorizontalAlignment(SwingConstants.CENTER);// �����Ͼ�����һ��
	    jt.setDefaultRenderer(Object.class, tcr);
		jsp=new JScrollPane(jt);
		
		//�²���
		jp2=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);
		
		//����BorderLayout����
		this.setLayout(new BorderLayout());
		this.add(jp1,"North");
		this.add(jsp,"Center");
		this.add(jp2,"South");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==jb1){
			//��ʾ�û����ѯ
			String name=this.jtf.getText().trim();
			buyerService.updateBuyerView(name);
		}else if(e.getSource()==jb2){
			//��ʾ�����û�  true: ����Dialog��ɲ�����������ִ�� false: ���� ����ִ��
			BuyerDialog buyerDialog=new BuyerDialog(null, "���Ӳɹ�Ա", true, null, 0, "add");
			buyerDialog.setVisible(true);
			//����
			buyerService.updateBuyerView(null);
		}else if(e.getSource()==jb4){
			//��ʾɾ���û�
			//getSelectedRow�᷵���û����е���
			//������û�һ�ж�ûѡ�񣬾ͷ���-1
			int rowNum=this.jt.getSelectedRow();
			if(rowNum==-1)
			{
				//��ʾ
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
				return ;//���ص�������
			}
			//�õ��û���� �õ���rowNum�е�0�У�Object->String��
			//��goodServiceΪ��ʼ���Ǹ�����ģ��
			System.out.println(rowNum+"��");
			String bno=buyerService.getValueAt(rowNum, 0).toString();
			int i=JOptionPane.showConfirmDialog(this, "ȷ��ɾ����?");
			if(i==0){
				//��ʾȷ��ɾ��
				if(buyerService.delBuyer(bno)){
					JOptionPane.showMessageDialog(this, "ɾ���ɹ�!");
				}else{
					JOptionPane.showMessageDialog(this, "ʧ�ܳɹ�!");
				}
			}
			//����
			buyerService.updateBuyerView(null);
		}else if(e.getSource()==jb3){
			//�û����޸�
			//ѡ��һ��
			int rowNums=this.jt.getSelectedRow();
			if(rowNums==-1){
				JOptionPane.showMessageDialog(this, "��ѡ��һ��");
				return;
			}
			BuyerDialog updUserView=new BuyerDialog(null, "�޸Ĳɹ�Ա", true, buyerService, rowNums,"update");
			updUserView.setVisible(true);
			//����
			buyerService.updateBuyerView(null);
		}	
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO �Զ����ɵķ������
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_ENTER){ 
			String data=jtf.getText().trim();
			buyerService.updateBuyerView(data);
        }  
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO �Զ����ɵķ������
		
	}

}