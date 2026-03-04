export const Authority = {
  ADMIN: 'ROLE_ADMIN',
  USER: 'ROLE_USER',
  MEDIC: 'ROLE_MEDIC',
  FARMACIST: 'ROLE_FARMACIST',
  PACIENT: 'ROLE_PACIENT',
} as const;

export type AuthorityType = (typeof Authority)[keyof typeof Authority];
