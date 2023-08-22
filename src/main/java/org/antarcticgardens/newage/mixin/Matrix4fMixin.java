package org.antarcticgardens.newage.mixin;

import com.mojang.math.Matrix4f;
import org.antarcticgardens.newage.tools.IMatrix4fValueOperator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Matrix4f.class)
public class Matrix4fMixin implements IMatrix4fValueOperator {
    @Shadow protected float m00, m01, m02, m03,
                            m10, m11, m12, m13,
                            m20, m21, m22, m23,
                            m30, m31, m32, m33;

    @Override
    public float[] create_new_age$getValues() {
        float[] values = new float[16];

        values[0] = m00;
        values[1] = m01;
        values[2] = m02;
        values[3] = m03;

        values[4] = m10;
        values[5] = m11;
        values[6] = m12;
        values[7] = m13;

        values[8] = m20;
        values[9] = m21;
        values[10] = m22;
        values[11] = m23;

        values[12] = m30;
        values[13] = m31;
        values[14] = m32;
        values[15] = m33;

        return values;
    }

    @Override
    public void create_new_age$setValues(float[] values) {
        m00 = values[0];
        m01 = values[1];
        m02 = values[2];
        m03 = values[3];

        m10 = values[4];
        m11 = values[5];
        m12 = values[6];
        m13 = values[7];

        m20 = values[8];
        m21 = values[9];
        m22 = values[10];
        m23 = values[11];

        m30 = values[12];
        m31 = values[13];
        m32 = values[14];
        m33 = values[15];
    }
}

