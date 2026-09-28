public class PlayerStateMachine
{
    public PlayerState currentState;
    public PlayerState previousState;

    public void Initialize(PlayerState startingState)
    {
        previousState = startingState;
        currentState = startingState;
        if(currentState != null)
            currentState.EnterState();
    }
    public void ChangeState(PlayerState newState)
    {
        previousState = currentState;
        if(previousState is PlayerDieState)
            return;
        if (newState != null)
        {
            
            if(newState is PlayerAttackState && currentState is PlayerLockInState)
            {
                PlayerLockInState lockIn = (PlayerLockInState)currentState;
                if(lockIn != null)
                    lockIn.ExitStateToFire();
            }else
            {
                if(currentState != null)
                currentState.ExitState();
            }
                
            
            currentState = newState;
            currentState.EnterState();
        }
        
    }
}