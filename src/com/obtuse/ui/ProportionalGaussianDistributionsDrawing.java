package com.obtuse.ui;

import com.obtuse.util.BasicProgramConfigInfo;
import com.obtuse.util.ProportionalGaussianDistribution;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * Draw one or more stacked proportionally weighted gaussian distributions.
 * <p/>
 * Copyright © 2012 Invidi Technologies Corporation
 * Copyright © 2012 Obtuse Systems Corporation
 */

public class ProportionalGaussianDistributionsDrawing extends JPanel {

    private ProportionalGaussianDistribution[] _gaussianDistributions;
    private final double _from;
    private final double _to;

    public ProportionalGaussianDistributionsDrawing() {
        //noinspection MagicNumber
        this( new ProportionalGaussianDistribution( 1.0, 0.5, 0.5 / 3 ) );

    }

    public ProportionalGaussianDistributionsDrawing( ProportionalGaussianDistribution gaussianDistribution ) {

        this( gaussianDistribution, 0.0, 1.0 );

    }

    public ProportionalGaussianDistributionsDrawing(
            ProportionalGaussianDistribution gaussianDistribution,
            double from,
            double to
    ) {

        this( new ProportionalGaussianDistribution[] { gaussianDistribution }, from, to );

    }

    public ProportionalGaussianDistributionsDrawing(
            ProportionalGaussianDistribution[] gaussianDistributions,
            double from,
            double to
    ) {
        super();

        _gaussianDistributions = Arrays.copyOf( gaussianDistributions, gaussianDistributions.length );
        _from = from;
        _to = to;

        //noinspection MagicNumber
        setMinimumSize( new Dimension( 400, 100 ) );
        //noinspection MagicNumber
        setMaximumSize( new Dimension( 400, 100 ) );

    }

    @SuppressWarnings("ConstantConditions")
    public void paint( Graphics g ) {

//        Graphics2D g2d = (Graphics2D)g;

//        Logger.logMsg( "painting gaussian distribution " + _gaussianDistribution + " in (" + getWidth() + "," + getHeight() + ")" );

        g.setColor( getBackground() );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        double maxY = 0.0;
        for ( int pX = 0; pX < getWidth(); pX += 1 ) {

            double rX = mapXtoDrawing( pX, 0, getWidth() - 1, _from, _to );
            double rY = 0.0;
            for ( ProportionalGaussianDistribution gd : _gaussianDistributions ) {

                rY += gd.getY( rX ) * gd.getWeight();

            }

            if ( rY > maxY ) {

                maxY = rY;

            }

        }

//        for ( ProportionalGaussianDistribution gd : _gaussianDistributions ) {
//
//            maxY += gd.getY( gd.getCenter() ) * gd.getWeight();
//
//        }

        if ( maxY == 0 ) {

            maxY = 1.0;

        }

        int height = getHeight();

        int[] x = new int[getWidth()];
        int[] y = new int[getWidth()];

        for ( int pX = 0; pX < getWidth(); pX += 1 ) {

            double rX = mapXtoDrawing( pX, 0, getWidth() - 1, _from, _to );
            double rY = 0.0;
            for ( ProportionalGaussianDistribution gd : _gaussianDistributions ) {

                rY += gd.getY( rX ) * gd.getWeight();

            }
//            Logger.logMsg( "pX = " + pX + ", rX = " + rX + ", rY = " + rY + ", scaled to " + ( rY / maxY ) );

            x[pX] = pX;
            y[pX] = (int)( height * ( 1.0 - rY / maxY ) );

//            g.drawLine( pX, 1 + (int)( height * ( rY / maxY ) ), pX, 1 + (int)( height * ( rY / maxY ) ) );

        }

        g.setColor( Color.BLACK );
        g.drawPolyline( x, y, getWidth() );

    }

    private double mapXtoDrawing( int pX, int minD, int maxD, double minR, double maxR ) {

        //noinspection UnnecessaryParentheses
        return minR + ( ( pX - minD ) * ( maxR - minR ) ) / ( maxD - minD );

    }

    public static void main( String[] args ) {

        BasicProgramConfigInfo.init( "Obtuse", "Shared", "GDD", null );

        JFrame xx = new JFrame();
        ProportionalGaussianDistributionsDrawing gdd = new ProportionalGaussianDistributionsDrawing();
        xx.setContentPane( gdd );
        xx.pack();
        xx.setVisible( true );

    }

    @SuppressWarnings("UnusedDeclaration")
    public void setDistribution( ProportionalGaussianDistribution gaussianDistribution ) {

        setDistributions( new ProportionalGaussianDistribution[] { gaussianDistribution } );

    }

    @SuppressWarnings("UnusedDeclaration")
    public void setDistributions( Collection<ProportionalGaussianDistribution> gaussianDistributions ) {

        _gaussianDistributions =
                gaussianDistributions.toArray( new ProportionalGaussianDistribution[gaussianDistributions.size()] );

        repaint();

    }

    public void setDistributions( ProportionalGaussianDistribution[] gaussianDistributions ) {

        _gaussianDistributions = Arrays.copyOf( gaussianDistributions, gaussianDistributions.length );
        repaint();

    }

}