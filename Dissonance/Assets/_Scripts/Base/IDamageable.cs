using System.Numerics;

public interface IDamageable
{
    void Damage(float damageAmount);
    virtual void Damage(float damageAmount, Enemy enemy){}
    virtual void Damage(float damageAmount, UnityEngine.Vector2 hitNormal, Enemy enemy){}
    virtual void Damage(float damageAmount, UnityEngine.Vector2 hitNormal){}
    void Die();

    float maxHealth { get; set; }
    float currentHealth { get; set; }
}