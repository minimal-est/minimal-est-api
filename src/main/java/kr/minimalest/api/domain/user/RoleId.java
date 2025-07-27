package kr.minimalest.api.domain.user;

public record RoleId (Long value){

       public RoleId {
               if (value == null)
                       throw new IllegalStateException("roleId는 null일 수 없습니다!");
       }

       public static RoleId of(Long value) {
               return new RoleId(value);
       }
}
