package cuchaz.modsShared.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cuchaz.modsShared.ColorUtils;


public class GuiBase extends GuiScreen {
	
	public static final int LeftMargin = 8;
	public static final int TopMargin = 6;
	public static final int LineSpacing = 2;

	public final int TextColor = ColorUtils.getGrey(64);
	public final int HeaderColor = ColorUtils.getColor(50, 99, 145);
	public final int HeaderBevelColor = ColorUtils.getColor(213, 223, 239);
	public final int YesColor = ColorUtils.getColor(0, 160, 0);
	public final int NoColor = ColorUtils.getColor(160, 0, 0);
	
	private int m_left;
	private int m_top;
	protected int m_width;
	protected int m_height;
	protected ResourceLocation m_background;
	private boolean m_darkenScreen;
	
	protected GuiBase(int width, int height, ResourceLocation background, boolean darkenScreen) {
		m_width = width;
		m_height = height;
		m_background = background;
		m_darkenScreen = darkenScreen;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		setPos(
			(this.width - m_width)/2,
			(this.height - m_height)/2
		);
	}
	
	protected void setPos(int left, int top) {
		m_left = left;
		m_top = top;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTickTime) {
		if (m_darkenScreen) {
			drawDefaultBackground();
		}
		
		// translate to the window rect
		GL11.glPushMatrix();
		GL11.glTranslatef(m_left, m_top, 0.0f);
		drawBackground(mouseX, mouseY, partialTickTime);
		GL11.glPopMatrix();
		
		// have to draw buttons in the full-screen coord system =(
		super.drawScreen(mouseX, mouseY, partialTickTime);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(m_left, m_top, 0.0f);
		drawForeground(mouseX, mouseY, partialTickTime);
		GL11.glPopMatrix();
	}
	
	protected void bindBackgroundTexture() {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(m_background);
	}
	
	protected void drawBackground(int mouseX, int mouseY, float partialTickTime) {
		bindBackgroundTexture();
		drawTexturedModalRect(0, 0, 0, 0, m_width, m_height);
	}
	
	protected void drawForeground(int mouseX, int mouseY, float partialTickTime) {
		// by default, do nothing
	}
	
	protected void drawHeaderText(String text, int lineNum) {
		int x1 = LeftMargin;
		int x2 = m_width - LeftMargin;
		int y1 = getLineY(lineNum);
		int y2 = y1 + this.mc.fontRenderer.FONT_HEIGHT;
		drawHorizontalLine(x1 - 1, x2 - 1, y2 - 2, HeaderBevelColor);
		this.mc.fontRenderer.drawString(text, x1 - 1, y1 - 1, HeaderBevelColor);
		drawHorizontalLine(x1, x2, y2 - 1, HeaderColor);
		this.mc.fontRenderer.drawString(text, x1, y1, HeaderColor);
	}
	
	protected void drawText(String text, int lineNum) {
		drawText(text, lineNum, TextColor);
	}
	
	protected void drawText(String text, int lineNum, int color) {
		this.mc.fontRenderer.drawString(text, LeftMargin, getLineY(lineNum), color);
	}
	
	protected void drawWrappedText(String text, int lineNum, int width) {
		this.mc.fontRenderer.drawSplitString(text, LeftMargin, getLineY(lineNum), m_width, TextColor);
	}
	
	protected void drawLabelValueText(String labelText, String valueText, int lineNum) {
		// draw the label
		this.mc.fontRenderer.drawString(labelText + ":", LeftMargin, getLineY(lineNum), TextColor);
		
		// draw the value
		int valueWidth = this.mc.fontRenderer.getStringWidth(valueText);
		this.mc.fontRenderer.drawString(valueText, m_width - LeftMargin - valueWidth, getLineY(lineNum), TextColor);
	}
	
	protected void drawYesNoText(String labelText, String valueText, boolean isYes, int lineNum) {
		// draw the label
		this.mc.fontRenderer.drawString(labelText + ":", LeftMargin, getLineY(lineNum), TextColor);
		
		// draw the value
		int valueColor = isYes ? YesColor : NoColor;
		int valueWidth = this.mc.fontRenderer.getStringWidth(valueText);
		this.mc.fontRenderer.drawString(valueText, m_width - LeftMargin - valueWidth, getLineY(lineNum), valueColor);
	}
	
	protected int getLineY(int lineNum) {
		return TopMargin + (this.mc.fontRenderer.FONT_HEIGHT + LineSpacing) * lineNum;
	}
	
	public void close() {
		mc.thePlayer.closeScreen();
		mc.setIngameFocus();
	}
	
	protected GuiButton newGuiButton(int id, int left, int top, int width, int height, String text) {
		return new GuiButton(id, m_left + left, m_top + top, width, height, text);
	}
}
