import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

export const authAdminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService); 
  const router = inject(Router);          

  if (authService.isUserAdmin()) {
    return true; 
  }

  router.navigate(['/unauthorized']);
  return false;
};
